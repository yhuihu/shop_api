package com.study.shop.business.mq;

import com.study.shop.provider.api.TbOrderService;
import com.study.shop.provider.domain.TbOrder;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

import java.util.List;

/**
 * @author Tiger
 * @date 2020-03-20
 * @see com.study.shop.business.mq
 **/
@RocketMQTransactionListener(txProducerGroup = "shop-group")
public class MqListen implements RocketMQLocalTransactionListener {
    @Reference(version = "1.0.0")
    private TbOrderService tbOrderService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        //解析消息内容
        String jsonString = new String((byte[]) message.getPayload());
        List<TbOrder> byGroup = tbOrderService.getByGroup(Long.valueOf(jsonString));
        if (byGroup.size() > 0) {
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        String jsonString = new String((byte[]) message.getPayload());
        List<TbOrder> byGroup = tbOrderService.getByGroup(Long.valueOf(jsonString));
        if (byGroup.size() > 0) {
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }
}
