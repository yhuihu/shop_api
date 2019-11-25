package com.study.shop.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tiger
 * @date 2019-11-20
 * @see com.study.shop.business
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class ClassificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClassificationApplication.class, args);
    }
}
