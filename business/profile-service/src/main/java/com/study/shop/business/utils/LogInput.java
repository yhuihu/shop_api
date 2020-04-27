package com.study.shop.business.utils;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Tiger
 * @date 2020-04-17
 * @see com.study.shop.business.utils
 **/
public interface LogInput {
    String INPUT = "log";

    /**
     * 日志通道
     * @return channel
     */
    @Input("log")
    SubscribableChannel log();
}
