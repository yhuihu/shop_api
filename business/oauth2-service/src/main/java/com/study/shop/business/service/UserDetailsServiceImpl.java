package com.study.shop.business.service;

import com.google.common.collect.Lists;
import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbUser;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Tiger
 * @date 2019-09-11
 * @see com.study.shop.business.service
 **/
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Reference(version = "1.0.0")
    private TbUserService tbUserService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 查询用户
        TbUser tbUser = tbUserService.get(s);
        // 用户存在
        if (tbUser != null) {
            List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
            if (tbUser.getStatus() == 0) {
                throw new BusinessException(ExceptionStatus.ACCOUNT_LOCK);
            }
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(tbUser.getRole());
            grantedAuthorities.add(grantedAuthority);
            return new User(tbUser.getUsername(), tbUser.getPassword(), grantedAuthorities);
        }
        // 用户不存在
        else {
            return null;
        }
    }
}
