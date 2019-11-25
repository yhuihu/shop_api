package com.study.shop.business.feign.fallback;

import com.study.shop.business.dto.params.IconParam;
import com.study.shop.business.dto.params.PasswordParam;
import com.study.shop.business.dto.params.ProfileParam;
import com.study.shop.business.feign.ProfileFeign;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.utils.MapperUtils;
import org.springframework.stereotype.Component;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business.feign.fallback
 **/
@Component
public class ProfileFeignFallback implements ProfileFeign {

    public static final String BREAKING_MESSAGE = "请求失败了，请检查您的网络";

    @Override
    public String info() {
        try {
            return MapperUtils.obj2json(new ResponseResult<Void>(ResponseResult.CodeStatus.BREAKING, BREAKING_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String update(ProfileParam profileParam) {
        try {
            return MapperUtils.obj2json(new ResponseResult<Void>(ResponseResult.CodeStatus.BREAKING, BREAKING_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String modifyPassword(PasswordParam passwordParam) {
        try {
            return MapperUtils.obj2json(new ResponseResult<Void>(ResponseResult.CodeStatus.BREAKING, BREAKING_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String modifyIcon(IconParam iconParam) {
        try {
            return MapperUtils.obj2json(new ResponseResult<Void>(ResponseResult.CodeStatus.BREAKING, BREAKING_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
