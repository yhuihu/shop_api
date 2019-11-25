package com.study.shop.business.feign;

import com.study.shop.business.dto.params.IconParam;
import com.study.shop.business.dto.params.PasswordParam;
import com.study.shop.business.dto.params.ProfileParam;
import com.study.shop.business.feign.fallback.ProfileFeignFallback;
import com.study.shop.configuration.FeignRequestConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business.feign
 **/
@FeignClient(value = "profile", path = "profile", configuration = FeignRequestConfiguration.class, fallback = ProfileFeignFallback.class)
public interface ProfileFeign {
    /**
     * 获取个人信息
     *
     * @return {@code String} JSON
     */
    @GetMapping(value = "info")
    String info();

    /**
     * 更新个人信息
     *
     * @param profileParam {@link ProfileParam}
     * @return {@code String} JSON
     */
    @PostMapping(value = "update")
    String update(@RequestBody ProfileParam profileParam);

    /**
     * 修改密码
     *
     * @param passwordParam {@link PasswordParam}
     * @return {@code String} JSON
     */
    @PostMapping(value = "modify/password")
    String modifyPassword(@RequestBody PasswordParam passwordParam);

    /**
     * 修改头像
     *
     * @param iconParam {@link IconParam}
     * @return {@code String} JSON
     */
    @PostMapping(value = "modify/icon")
    String modifyIcon(@RequestBody IconParam iconParam);
}
