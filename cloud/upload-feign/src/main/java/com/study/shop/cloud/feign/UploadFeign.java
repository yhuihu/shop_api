package com.study.shop.cloud.feign;

import com.study.shop.cloud.feign.fallback.UploadFeignFallback;
import com.study.shop.configuration.FeignRequestConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.cloud.feign
 **/
@FeignClient(value = "cloud-upload", path = "upload", configuration = FeignRequestConfiguration.class,fallback = UploadFeignFallback.class)
public interface UploadFeign {

    /**
     * 文件上传
     *
     * @param multipartFile {@code MultipartFile}
     * @return {@code String} 文件上传路径
     */
    @PostMapping(value = "")
    String upload(@RequestPart(value = "multipartFile") MultipartFile multipartFile);
}
