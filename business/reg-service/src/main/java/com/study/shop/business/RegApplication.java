package com.study.shop.business;

import com.study.shop.configuration.DataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Admin
 */
@SpringBootApplication(scanBasePackageClasses = {RegApplication.class, DataSourceConfig.class})
public class RegApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegApplication.class, args);
    }
}
