package com.study.shop.business;

import com.study.shop.configuration.DataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tiger
 * @date 2019-12-06
 * @see com.study.shop.business
 **/
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackageClasses = {GoodsApplication.class, DataSourceConfig.class})
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }
}
