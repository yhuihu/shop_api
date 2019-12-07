package com.study.shop.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Tiger
 * @date 2019-09-20
 * @see com.study.shop.business
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class OAuth2ApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void test_centos(){
        User user= (User) redisTemplate.opsForValue().get("auth:0cf3f278-1155-40bd-ac82-10fb55f04eba");
        System.out.println(user);
    }

}
