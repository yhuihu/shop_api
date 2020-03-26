package com.study.shop.business.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.business.controller.fallback.ProfileControllerFallback;
import com.study.shop.business.dto.UserInfo;
import com.study.shop.business.dto.params.IconParam;
import com.study.shop.business.dto.params.PasswordParam;
import com.study.shop.business.dto.params.ProfileParam;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.dto.FindDTO;
import com.study.shop.provider.dto.UserDTO;
import com.study.shop.utils.SnowIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping(value = "profile")
@Slf4j
public class ProfileController {

    @Reference(version = "1.0.0")
    private TbUserService tbUserService;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @PostMapping("register")
    public ResponseResult register(@RequestBody UserDTO userDTO) {
        if ("anonymousUser".equals(userDTO.getUsername())) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "非法用户名！");
        }
        String redisCode = redisTemplate.opsForValue().get("register_" + userDTO.getEmail());
        redisCode = getReplace(redisCode);
        if (redisCode == null) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "请先获取验证码！");
        } else if (!redisCode.equals(userDTO.getCode())) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "验证码错误！");
        }
        TbUser tbUser = new TbUser();
        BeanUtils.copyProperties(userDTO, tbUser);
        tbUser.setId(SnowIdUtils.uniqueLong());
        try {
            int insert = tbUserService.insert(tbUser);
            if (insert > 0) {
                redisTemplate.delete("register_" + userDTO.getEmail());
                return new ResponseResult<>(ResponseResult.CodeStatus.OK, "注册成功！");
            } else if (insert == -1) {
                return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "手机号已存在！");
            } else {
                throw new BusinessException(ExceptionStatus.DATABASE_ERROR);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(ExceptionStatus.DATABASE_ERROR);
        }
    }

    @NotNull
    private String getReplace(String redisCode) {
        return redisCode.replace("\"", "");
    }

    @PostMapping("reset")
    public ResponseResult findUser(@RequestBody FindDTO findDTO) {
        String redisCode = redisTemplate.opsForValue().get("find_" + findDTO.getEmail());
        redisCode = getReplace(redisCode);
        if (redisCode == null) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "请先获取验证码！");
        } else if (!redisCode.equals(findDTO.getCode())) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "验证码错误！");
        }

        try {
            int aim = tbUserService.findUser(findDTO.getEmail(), findDTO.getPassword());
            if (aim > 0) {
                return new ResponseResult<>(ResponseResult.CodeStatus.OK, "密码修改成功！");
            } else {
                throw new BusinessException(ExceptionStatus.DATABASE_ERROR);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(ExceptionStatus.DATABASE_ERROR);
        }
    }

    /**
     * 获取个人信息
     *
     * @return {@link ResponseResult}
     */
    @GetMapping(value = "info")
    @SentinelResource(value = "info", fallback = "infoFallback", fallbackClass = ProfileControllerFallback.class)
    public ResponseResult<UserInfo> info() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TbUser tbUser = tbUserService.get(authentication.getName());
        UserInfo dto = new UserInfo();
        BeanUtils.copyProperties(tbUser, dto);
        dto.setId(String.valueOf(tbUser.getId()));
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
        if (oldTbUser == null) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "异常操作");
        }
        BeanUtils.copyProperties(profileParam, oldTbUser);
        oldTbUser.setUpdateTime(new Date());
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
