package com.study.shop.business.controller;

import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.business.mq.MqUtils;
import com.study.shop.business.util.OrderDetail;
import com.study.shop.business.util.OrderList;
import com.study.shop.business.util.RedisLock;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbCartService;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.api.TbOrderService;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbOrder;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.vo.GoodDetailVO;
import com.study.shop.utils.SnowIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Tiger
 * @date 2020-02-13
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("order")
@Slf4j
public class OrderController {
    @Reference(version = "1.0.0")
    private TbUserService tbUserService;
    @Reference(version = "1.0.0")
    private TbItemService tbItemService;
    @Reference(version = "1.0.0")
    private TbOrderService tbOrderService;
    @Reference(version = "1.0.0")
    TbCartService tbCartService;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private MqUtils mqUtils;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 12, 5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    private ThreadPoolExecutor lockPool = new ThreadPoolExecutor(2, 8, 30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    /**
     * 下单api
     *
     * @param orderList 商品编号集合
     * @return {@link ResponseResult}
     */
    @PostMapping()
    public ResponseResult<String> createOrder(@RequestBody OrderList orderList) {
        List<String> goodsIds = new ArrayList<>();
        orderList.getOrderDetails().forEach(item -> {
            goodsIds.add(item.getGoodsId());
        });
        List<Future<GoodDetailVO>> goodsDetailFutureList = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<String> lockList = new ArrayList<>();
        Runnable runnable = () -> {
            while (true) {
                try {
                    for (String s : lockList) {
                        redisLock.getLock(s, username);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error(e.getLocalizedMessage());
                }
            }
        };
        Long groupId = SnowIdUtils.uniqueLong();
        try {
            lockPool.execute(runnable);
            int size = goodsIds.size();
            long start = System.currentTimeMillis();
            for (int i = 0; i < size; ) {
                if (redisLock.getLock(goodsIds.get(i), username)) {
                    lockList.add(goodsIds.get(i++));
                }
                if (System.currentTimeMillis() - start > 30000) {
                    throw new BusinessException(ExceptionStatus.ORDER_ERROR);
                }
            }
            goodsIds.forEach(item -> {
                goodsDetailFutureList.add(threadPoolExecutor.submit(() -> tbItemService.getGoodDetail(Long.valueOf(item))));
            });
            List<Long> goodsList = new ArrayList<>();
            for (Future<GoodDetailVO> goodDetailFuture : goodsDetailFutureList) {
                GoodDetailVO goodDetailVO = goodDetailFuture.get();
                if (goodDetailVO.getStatus() == 2 || goodDetailVO.getStatus() == 3) {
                    return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, goodDetailVO.getTitle() + "已被其他用户拍下");
                }
                goodsList.add(goodDetailVO.getId());
            }
            Date date = new Date();
            TbUser tbUser = tbUserService.get(username);
            Long userId = tbUser.getId();
            // 生成订单
            for (OrderDetail item : orderList.getOrderDetails()) {
                TbOrder tbOrder = new TbOrder();
                BeanUtils.copyProperties(orderList, tbOrder);
                tbOrder.setId(SnowIdUtils.uniqueLong());
                tbOrder.setGroupId(groupId);
                BeanUtils.copyProperties(item, tbOrder);
                tbOrder.setGoodsId(Long.valueOf(item.getGoodsId()));
                tbOrder.setCreateTime(date);
                tbOrder.setUserId(userId);
                tbOrder.setStatus(0);
                int i = tbOrderService.addOrder(tbOrder);
                if (i == 0) {
                    return new ResponseResult(ResponseResult.CodeStatus.FAIL, "出现未知错误，请稍后重试");
                }
            }
            // 修改商品状态
            int i = tbItemService.changeGoodsStatus(goodsList, 3);
            if (i == 0) {
                throw new BusinessException(ExceptionStatus.DATABASE_ERROR);
            }
            // 购物车处理
            tbCartService.deleteCheck(username);
            return new ResponseResult(ResponseResult.CodeStatus.OK, "生成订单成功", String.valueOf(groupId));
        } catch (Exception e) {
            log.error(e.getMessage());
//            发送mq消息回滚
            mqUtils.createOrderMessage(String.valueOf(groupId));
            return new ResponseResult(ResponseResult.CodeStatus.FAIL, "当前人数过多，请稍后重试");
        } finally {
            for (String goodsId : goodsIds) {
                redisLock.releaseLock(goodsId, username);
            }
            lockPool.remove(runnable);
        }
    }

    @GetMapping("byGroup/{groupId}")
    public ResponseResult getByGroup(@PathVariable(value = "groupId") String groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        return new ResponseResult(ResponseResult.CodeStatus.OK, "查询成功", tbOrderService.getByGroupCheck(Long.valueOf(groupId), tbUser.getId()));
    }

    @PutMapping("/{groupId}")
    public ResponseResult checkOrder(@PathVariable(value = "groupId") String groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        List<TbOrder> byGroup = tbOrderService.getByGroup(Long.valueOf(groupId));
        try {
//            根据group去更新订单状态
            int i = tbOrderService.checkOrder(tbUser.getId(), Long.valueOf(groupId));
            if (i == 0) {
                throw new BusinessException(ExceptionStatus.DATABASE_ERROR);
            }
            List<Long> goodsList = new ArrayList<>();
            byGroup.forEach(item -> {
                goodsList.add(item.getGoodsId());
            });
//            修改商品状态为已售出
            int i1 = tbItemService.changeGoodsStatus(goodsList, 2);
            if (i1 == 0) {
                throw new BusinessException(ExceptionStatus.DATABASE_ERROR);
            }
            return new ResponseResult(ResponseResult.CodeStatus.OK, "确认订单成功");
        } catch (Exception e) {
            log.error("确认订单出现错误：{}", e.getMessage());
//            发送mq消息回滚
            mqUtils.checkOrderMessage(groupId);
            return new ResponseResult(ResponseResult.CodeStatus.FAIL, "出现未知错误");
        }
    }
}
