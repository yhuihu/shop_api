package com.study.shop.business.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author Tiger
 * @date 2020-03-20
 * @see com.study.shop.business.mq
 **/
public interface ShopSource {
    /**
     * 发送消息时用
     * @return answer
     */
    @Output("shop")
    MessageChannel shop();
}
