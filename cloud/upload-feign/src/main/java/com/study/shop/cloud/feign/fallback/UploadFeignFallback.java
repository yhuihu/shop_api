package com.study.shop.cloud.feign.fallback;

import com.study.shop.cloud.feign.UploadFeign;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.utils.MapperUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.cloud.feign.fallback
 **/
@Component
public class UploadFeignFallback implements UploadFeign {

    private static final String BREAKING_MESSAGE = "请求失败了，请检查您的网络";

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            return MapperUtils.obj2json(new ResponseResult<Void>(ResponseResult.CodeStatus.BREAKING, BREAKING_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
