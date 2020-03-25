package com.study.shop.business;

import com.study.shop.configuration.DataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import com.study.shop.configuration.RedisConfig;

/**
 * EnableDiscoveryClient 让注册中心能够发现，扫描到改服务
 *
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business
 **/
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackageClasses = {RedisConfig.class, ProfileApplication.class, DataSourceConfig.class})
public class ProfileApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProfileApplication.class, args);
    }
}
