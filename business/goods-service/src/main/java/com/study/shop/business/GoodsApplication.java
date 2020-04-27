package com.study.shop.business;

import com.study.shop.business.mq.OrderInput;
import com.study.shop.business.mq.ShopSource;
import com.study.shop.commons.aop.aspect.LogAspect;
import com.study.shop.configuration.DataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Tiger
 * @date 2019-12-06
 * @see com.study.shop.business
 **/
@EnableBinding({ShopSource.class, OrderInput.class})
@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication(scanBasePackageClasses = {GoodsApplication.class, DataSourceConfig.class, LogAspect.class})
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }
}
