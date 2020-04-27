package com.study.shop.business;

import com.study.shop.commons.aop.aspect.LogAspect;
import com.study.shop.commons.aop.configuration.RocketMQConfiguration;
import com.study.shop.configuration.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Tiger
 * @description //TODO
 * @date 2019-09-11
 **/
@SpringBootApplication(scanBasePackageClasses = {OAuth2Application.class, RedisConfig.class, RocketMQConfiguration.class, LogAspect.class})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
public class OAuth2Application {
    public static void main(String[] args) {
        SpringApplication.run(OAuth2Application.class, args);
    }
}
