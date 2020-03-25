package com.study.shop.business.mq;

import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Tiger
 * @date 2020-03-20
 * @see com.study.shop.business.mq
 **/
@Component
public class MqUtils {
    @Resource
    ShopSource source;

    public void createOrderMessage(String message) {
        source.shop().send(MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.TAGS, "create_order").build());
    }

    public void checkOrderMessage(String message) {
        source.shop().send(MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.TAGS, "check_order").build());
    }
}
