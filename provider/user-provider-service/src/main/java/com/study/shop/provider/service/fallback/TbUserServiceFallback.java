package com.study.shop.provider.service.fallback;

import com.study.shop.provider.domain.TbUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Admin
 */
public class TbUserServiceFallback {

    private static final Logger logger = LoggerFactory.getLogger(TbUserServiceFallback.class);

    /**
     * 熔断方法
     *
     * @param username {@code String} 用户名
     * @param ex       {@code Throwable} 异常信息
     * @return {@link TbUser} 熔断后的固定结果
     */
    public static TbUser getByUsernameFallback(String username, Throwable ex) {
        logger.warn("Invoke getByUsernameFallback: " + ex.getClass().getTypeName());
        ex.printStackTrace();
        return null;
    }

}
