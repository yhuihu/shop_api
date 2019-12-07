package com.study.shop.provider;

import com.study.shop.configuration.DubboSentinelConfiguration;
import com.study.shop.configuration.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Tiger
 * @date 2019-12-06
 * @see com.study.shop.provider
 **/
@SpringBootApplication(scanBasePackageClasses = {GoodsProviderApplication.class, DubboSentinelConfiguration.class, RedisConfig.class})
@MapperScan(basePackages = "com.study.shop.provider.mapper")
@EnableCaching
public class GoodsProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsProviderApplication.class, args);
    }
}
