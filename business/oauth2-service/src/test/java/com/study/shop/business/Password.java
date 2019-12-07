package com.study.shop.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Tiger
 * @date 2019-09-11
 * @see com.study.shop.business
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class Password {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void getPassword(){
        System.out.println(passwordEncoder.encode("yang526163"));
    }
    @Test
    public void testCentOS(){
        System.out.println(redisTemplate.opsForValue().get("adminUser"));
    }
}
