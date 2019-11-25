package com.study.shop.provider;

import com.study.shop.configuration.DubboSentinelConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Admin
 */
@SpringBootApplication(scanBasePackageClasses = {UserProviderApplication.class, DubboSentinelConfiguration.class})
@MapperScan(basePackages = "com.study.shop.provider.mapper")
public class UserProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserProviderApplication.class, args);
    }
}
