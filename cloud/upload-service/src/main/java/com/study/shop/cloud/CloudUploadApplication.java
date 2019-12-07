package com.study.shop.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.cloud
 **/
@SpringBootApplication(scanBasePackageClasses = {CloudUploadApplication.class}, scanBasePackages = "com.study.shop.business")
@EnableDiscoveryClient
public class CloudUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudUploadApplication.class, args);
    }
}
