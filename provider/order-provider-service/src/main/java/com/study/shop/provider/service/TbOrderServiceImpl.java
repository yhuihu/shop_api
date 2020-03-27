package com.study.shop.provider.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.shop.provider.api.TbOrderService;
import com.study.shop.provider.domain.TbOrder;
import com.study.shop.provider.dto.OrderListDTO;
import com.study.shop.provider.mapper.TbOrderMapper;
import com.study.shop.provider.vo.CheckOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Slf4j
@Service(version = "1.0.0", parameters = {"addOrder.retries", "0", "addOrder.timeout", "30000"})
public class TbOrderServiceImpl implements TbOrderService {

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Override
    public int addOrder(TbOrder tbOrder) {
        try {
            return tbOrderMapper.insert(tbOrder);
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public int checkOrder(Long userId, Long groupId, List<CheckOrderVO> checkOrderVOS) {
        try {
            Example example = new Example(TbOrder.class);
            example.createCriteria().andEqualTo("groupId", groupId).andEqualTo("userId", userId);
            example.setForUpdate(true);
            List<TbOrder> tbOrders = tbOrderMapper.selectByExample(example);
            Map<String, CheckOrderVO> map = checkOrderVOS.stream().collect(Collectors.toMap(CheckOrderVO::getGoodsId, checkOrderVO -> checkOrderVO));
            tbOrders.forEach(item -> {
                item.setStatus(2);
                item.setPaymentTime(new Date());
                item.setPayment(map.get(String.valueOf(item.getGoodsId())).getPrice().doubleValue());
                tbOrderMapper.updateByPrimaryKeySelective(item);
            });
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public List<CheckOrderVO> getByGroupCheck(Long groupId, Long userId) {
        return tbOrderMapper.getCheckByGroupId(groupId, userId);
    }

    @Override
    public List<TbOrder> getByGroup(Long groupId) {
        Example example = new Example(TbOrder.class);
        example.createCriteria().andEqualTo("groupId", groupId).andEqualTo("status", 0);
        return tbOrderMapper.selectByExample(example);
    }

    @Override
    public int deleteByGroup(Long groupId) {
        Example example = new Example(TbOrder.class);
        example.createCriteria().andEqualTo("groupId", groupId).andEqualTo("status", 0);
        return tbOrderMapper.deleteByExample(example);
    }

    @Override
    public List<TbOrder> getTimeOutOrder(Date date) {
        Example example = new Example(TbOrder.class);
        example.createCriteria().andLessThan("createTime", date).andEqualTo("status", 0);
        return tbOrderMapper.selectByExample(example);
    }

    @Override
    public int changeOrderStatus(Long groupId, Integer status) {
        TbOrder tbOrder = new TbOrder();
        tbOrder.setStatus(status);
        Example example = new Example(TbOrder.class);
        example.createCriteria().andEqualTo("groupId", groupId);
        tbOrderMapper.updateByExampleSelective(tbOrder, example);
        return 0;
    }

    @Override
    public PageInfo<OrderListDTO> getMyOrder(Long userId, Integer page, Integer size) {
        Example example = new Example(TbOrder.class);
        example.createCriteria().andEqualTo("userId", userId);
        PageHelper.startPage(page, size);
        List<OrderListDTO> tbOrders = tbOrderMapper.getMyOrder(userId);
        return new PageInfo<>(tbOrders);
    }

    @Override
    public int payOrder(Long orderId, Long userId, Long goodsId, BigDecimal price) {
        TbOrder tbOrder = new TbOrder();
        tbOrder.setStatus(2);
        tbOrder.setPayment(price.doubleValue());
        Example example = new Example(TbOrder.class);
        example.createCriteria().andEqualTo("userId", userId).
                andEqualTo("id", orderId).andEqualTo("goodsId", goodsId);
        return tbOrderMapper.updateByExampleSelective(tbOrder, example);
    }

    @Override
    public int deleteOrder(Long orderId, Long userId) {
        Example example = new Example(TbOrder.class);
        example.createCriteria().andEqualTo("userId", userId).andEqualTo("id", orderId).andEqualTo("status", 0);
        return tbOrderMapper.deleteByExample(example);
    }
}
