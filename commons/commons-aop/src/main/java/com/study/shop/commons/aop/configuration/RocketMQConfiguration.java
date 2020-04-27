package com.study.shop.commons.aop.configuration;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tiger
 * @date 2020-04-17
 * @see com.study.shop.commons.aop.configuration
 **/
@Configuration
public class RocketMQConfiguration {
    /**
     * 注入一个默认的消费者
     * @return
     * @throws MQClientException
     */
    @Bean
    public DefaultMQProducer getRocketMQProducer() throws MQClientException {
        DefaultMQProducer producer;
        producer = new DefaultMQProducer("log-group");
        producer.setNamesrvAddr("192.168.200.128:9876");
        try {
            producer.start();
        } catch (MQClientException e) {
            throw e;
        }
        return producer;
    }
}
