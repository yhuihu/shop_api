package com.study.shop.provider.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Tiger
 * @date 2020-04-17
 * @see com.study.shop.provider.vo
 **/
@Data
public class AdminUserVO {
    private String id;
    private String username;
    private String nickName;
    private String phone;
    private String email;
    private String address;
    private Integer status;
    private Date createTime;
}
