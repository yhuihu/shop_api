package com.study.shop.provider;

import com.study.shop.configuration.DataSourceConfig;
import com.study.shop.configuration.DubboSentinelConfiguration;
import com.study.shop.configuration.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Tiger
 * @date 2020-03-19
 * @see com.study.shop.provider
 **/
@SpringBootApplication(scanBasePackageClasses = {OrderProviderApplication.class, DubboSentinelConfiguration.class, RedisConfig.class, DataSourceConfig.class})
@MapperScan(basePackages = "com.study.shop.provider.mapper")
public class OrderProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderProviderApplication.class, args);
    }
}
