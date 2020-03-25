package com.study.shop.business.configure;

import com.study.shop.business.websocket.RedisConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author Tiger
 * @date 2020-03-22
 * @see com.study.shop.business.configure
 **/
@Configuration
public class RedisChannel {
    @Autowired
    RedisTemplate redisTemplate;

    //    注册消息订阅通道
    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new RedisConsumer());
    }

    @Bean
    RedisMessageListenerContainer container(MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        container.addMessageListener(listenerAdapter, new PatternTopic("CHAT_CHANNEL"));
        return container;
    }
}
