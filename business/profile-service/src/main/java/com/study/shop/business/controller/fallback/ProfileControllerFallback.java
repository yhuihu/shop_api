package com.study.shop.business.controller.fallback;

import com.study.shop.business.dto.UserInfo;
import com.study.shop.business.feign.fallback.ProfileFeignFallback;
import com.study.shop.commons.dto.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business.controller.fallback
 **/
public class ProfileControllerFallback {

    private static final Logger logger = LoggerFactory.getLogger(ProfileControllerFallback.class);

    /**
     * 熔断方法
     *
     * @param username {@code String} 用户名
     * @return {@link ResponseResult< UserInfo >}
     */
    public static ResponseResult<UserInfo> infoFallback(String username, Throwable ex) {
        logger.warn("Invoke infoFallback: " + ex.getClass().getTypeName());
        ex.printStackTrace();
        return new ResponseResult<UserInfo>(ResponseResult.CodeStatus.BREAKING, ProfileFeignFallback.BREAKING_MESSAGE);
    }

}
