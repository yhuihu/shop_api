package com.study.shop.business;

import com.study.shop.business.utils.LogInput;
import com.study.shop.commons.aop.aspect.LogAspect;
import com.study.shop.commons.aop.configuration.RocketMQConfiguration;
import com.study.shop.configuration.DataSourceConfig;
import com.study.shop.configuration.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * EnableDiscoveryClient 让注册中心能够发现，扫描到改服务
 *
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business
 **/
@EnableBinding({LogInput.class})
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackageClasses = {RedisConfig.class, ProfileApplication.class, DataSourceConfig.class, RocketMQConfiguration.class, LogAspect.class})
public class ProfileApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProfileApplication.class, args);
    }
}
