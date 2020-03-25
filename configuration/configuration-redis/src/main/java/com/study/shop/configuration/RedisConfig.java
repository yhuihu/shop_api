package com.study.shop.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tiger
 * @date 2019-09-20
 * @see com.study.shop.business.configure
 **/
@Configuration
public class RedisConfig {
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(8);
        genericObjectPoolConfig.setMinIdle(0);
        genericObjectPoolConfig.setMaxTotal(8);
        genericObjectPoolConfig.setMaxWaitMillis(-1);
        return genericObjectPoolConfig;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {
        RedisClusterConfiguration redisConfiguration = new RedisClusterConfiguration();
        List<RedisNode> nodeList = new ArrayList<>();
        nodeList.add(new RedisNode("192.168.200.128", 7001));
        nodeList.add(new RedisNode("192.168.200.128", 7002));
        nodeList.add(new RedisNode("192.168.200.128", 7003));
        nodeList.add(new RedisNode("192.168.200.128", 7004));
        nodeList.add(new RedisNode("192.168.200.128", 7005));
        nodeList.add(new RedisNode("192.168.200.128", 7006));
        redisConfiguration.setClusterNodes(nodeList);
        redisConfiguration.setPassword("yang123(*&");
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .build();
        return new LettuceConnectionFactory(redisConfiguration, clientConfig);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
