package com.study.shop.provider;

import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.mapper.TbUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProviderApplicationTests {
    @Autowired
    TbUserMapper tbUserMapper;

    @Test
    public void test(){
        tbUserMapper.selectAll().forEach(System.out::println);
    }

    @Test
    public void insertTest(){
        TbUser tuser =new TbUser();
    }
}
