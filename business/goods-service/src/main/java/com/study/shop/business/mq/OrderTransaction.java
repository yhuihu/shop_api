package com.study.shop.business.mq;

import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.api.TbOrderService;
import com.study.shop.provider.domain.TbOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tiger
 * @date 2020-03-20
 * @see com.study.shop.business.mq
 **/
@Component
@Slf4j
public class OrderTransaction {
    @Reference(version = "1.0.0")
    private TbOrderService tbOrderService;
    @Reference(version = "1.0.0")
    private TbItemService tbItemService;

//    改进方案@StreamListener(value = MySink.USER_CHANNEL, condition = "headers['flag']=='aa'")
    @StreamListener(OrderInput.INPUT)
    public void orderTransaction(@Payload String receiveMsg, @Header(name = "rocketmq_TAGS") Object name) {
        if ("create_order".equals(name)) {
            try {
                Long groupId = Long.valueOf(receiveMsg);
                List<TbOrder> byGroup = tbOrderService.getByGroup(groupId);
                if (byGroup != null) {
                    List<Long> goodsId = new ArrayList<>();
                    byGroup.forEach(item -> {
                        goodsId.add(item.getGoodsId());
                    });
                    // 修改商品状态
                    tbItemService.changeGoodsStatus(goodsId, 1);
                    // 删除预订单
                    tbOrderService.deleteByGroup(groupId);
                }
            } catch (Exception e) {
                log.error("创建订单分布式事务执行失败" + e.getMessage());
                throw new RuntimeException();
            }
        } else if ("check_order".equals(name)) {
            try {
                Long groupId = Long.valueOf(receiveMsg);
                List<TbOrder> byGroup = tbOrderService.getByGroup(groupId);
                if (byGroup != null) {
                    List<Long> goodsId = new ArrayList<>();
                    byGroup.forEach(item -> {
                        goodsId.add(item.getGoodsId());
                    });
                    // 修改商品状态
                    tbItemService.changeGoodsStatus(goodsId, 3);
                    // 删除预订单
                    tbOrderService.changeOrderStatus(groupId,0);
                }
            } catch (Exception e) {
                log.error("确认订单分布式事务执行失败" + e.getMessage());
                throw new RuntimeException();
            }
        }
    }
}
