package com.study.shop.business.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录参数接收
 * @author Tiger
 * @date 2019-09-11
 * @see com.study.shop.business.dto
 **/
@Data
public class LoginParam implements Serializable {
    private String username;
    private String password;
}
