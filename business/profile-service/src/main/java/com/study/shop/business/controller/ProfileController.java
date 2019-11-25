package com.study.shop.business.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.study.shop.business.controller.fallback.ProfileControllerFallback;
import com.study.shop.business.dto.UserInfo;
import com.study.shop.business.dto.params.IconParam;
import com.study.shop.business.dto.params.PasswordParam;
import com.study.shop.business.dto.params.ProfileParam;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbUser;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping(value = "profile")
public class ProfileController {

    @Reference(version = "1.0.0")
    private TbUserService tbUserService;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 获取个人信息
     *
     * @return {@link ResponseResult}
     */
    @GetMapping(value = "info")
    @PreAuthorize("hasAuthority('USER')")
    @SentinelResource(value = "info", fallback = "infoFallback", fallbackClass = ProfileControllerFallback.class)
    public ResponseResult<UserInfo> info() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TbUser tbUser = tbUserService.get(authentication.getName());
        UserInfo dto = new UserInfo();
        BeanUtils.copyProperties(tbUser, dto);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取个人信息", dto);
    }

    /**
     * 更新个人信息
     *
     * @param profileParam {@link ProfileParam}
     * @return {@link ResponseResult}
     */
    @PostMapping(value = "update")
    public ResponseResult<Void> update(@RequestBody ProfileParam profileParam) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TbUser oldTbUser = tbUserService.get(authentication.getName());
        if(oldTbUser ==null){
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "异常操作");
        }
        BeanUtils.copyProperties(profileParam, oldTbUser);
        int result = tbUserService.update(oldTbUser);

        // 成功
        if (result > 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "更新个人信息成功");
        }

        // 失败
        else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "更新个人信息失败");
        }
    }

    /**
     * 修改密码
     *
     * @param passwordParam {@link PasswordParam}
     * @return {@link ResponseResult}
     */
    @PostMapping(value = "modify/password")
    public ResponseResult<Void> modifyPassword(@RequestBody PasswordParam passwordParam) {
        // 获取认证信息上下文,可以根据token获取用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TbUser tbUser = tbUserService.get(authentication.getName());

        // 旧密码正确
        if (passwordEncoder.matches(passwordParam.getOldPassword(), tbUser.getPassword())) {
            int result = tbUserService.modifyPassword(tbUser.getUsername(), passwordParam.getNewPassword());
            if (result > 0) {
                return new ResponseResult<>(ResponseResult.CodeStatus.OK, "修改密码成功");
            }
        }

        // 旧密码错误
        else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "旧密码不匹配，请重试");
        }

        return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "修改密码失败");
    }

    /**
     * 修改头像
     *
     * @param iconParam {@link IconParam}
     * @return {@link ResponseResult}
     */
    @PostMapping(value = "modify/icon")
    public ResponseResult<Void> modifyIcon(@RequestBody IconParam iconParam) {
        int result = tbUserService.modifyIcon(iconParam.getUsername(), iconParam.getPath());

        // 成功
        if (result > 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "更新头像成功");
        }

        // 失败
        else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "更新头像失败");
        }
    }

}
