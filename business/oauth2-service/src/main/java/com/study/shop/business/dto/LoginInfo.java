package com.study.shop.business.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business.dto
 **/
@Data
public class LoginInfo implements Serializable {
    private static final long serialVersionUID = 4073457798857479065L;
    private String id;
    private String name;
    private String avatar;
    private String nickName;
}
