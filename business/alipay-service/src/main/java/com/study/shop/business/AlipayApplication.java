package com.study.shop.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tiger
 * @date 2019-11-29
 * @see com.study.shop.business
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class AlipayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlipayApplication.class, args);
    }
}
