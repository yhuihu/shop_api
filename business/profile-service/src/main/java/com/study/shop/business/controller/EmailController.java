package com.study.shop.business.controller;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.utils.EmailTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Tiger
 * @date 2020-03-15
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping(value = "email")
@Slf4j
public class EmailController {
    @Reference(version = "1.0.0")
    private TbUserService tbUserService;

    @Autowired
    RedisTemplate redisTemplate;

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 发送邮箱验证码
     *
     * @param email 用户邮箱
     */
    @PostMapping("/{email}")
    public ResponseResult sendEmail(@PathVariable(name = "email") @Valid @Email String email) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        int byMail = tbUserService.getByMail(email);
        if (byMail > 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "该邮箱已存在！");
        }
        if (valueOperations.get("register_" + email) != null) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "请勿重复获取验证码");
        }
        Runnable runnable = () -> {
            try {
                String uuid = getUUID();
                if (valueOperations.get("register_" + email) == null) {
                    valueOperations.set("register_" + email, uuid, 5, TimeUnit.MINUTES);
                    EmailTool.send(email, "注册平台验证码", uuid);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                redisTemplate.delete("register_" + email);
            }
        };
        threadPoolExecutor.execute(runnable);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "请前往邮箱查看验证码");
    }

    /**
     * 利用邮箱找回密码
     *
     * @param email email
     */
    @PutMapping("/{email}")
    public ResponseResult findEmail(@PathVariable(name = "email") @Valid @Email String email) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        int byMail = tbUserService.getByMail(email);
        if (byMail < 1) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "该邮箱不存在对应账号，请核实后重试！");
        }
        if (valueOperations.get("find_" + email) != null) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "请勿重复获取验证码");
        }
        Runnable runnable = () -> {
            try {
                String uuid = getUUID();
                if (valueOperations.get("find_" + email) == null) {
                    valueOperations.set("find_" + email, uuid, 5, TimeUnit.MINUTES);
                    EmailTool.send(email, "找回密码", uuid);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                redisTemplate.delete("find_" + email);
            }
        };
        threadPoolExecutor.execute(runnable);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "请前往邮箱查看验证码");
    }

    private String getUUID() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder uuid = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            uuid.append(ch);
        }
        return uuid.toString();
    }
}
