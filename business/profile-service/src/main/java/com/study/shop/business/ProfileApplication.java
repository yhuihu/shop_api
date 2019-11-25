package com.study.shop.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * EnableDiscoveryClient 让注册中心能够发现，扫描到改服务
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class ProfileApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProfileApplication.class,args);
    }
}
