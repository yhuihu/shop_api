package com.study.shop.business.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Tiger
 * @date 2020-03-20
 * @see com.study.shop.business.mq
 **/
public interface OrderInput {
    String INPUT = "order";
    /**
     * 接收消息实现分布式事务
     * @return 订单group
     */
    @Input("order")
    SubscribableChannel order();
}
