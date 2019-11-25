package com.study.shop.business.dto.params;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business.dto.params
 **/
@Data
public class IconParam implements Serializable {

    private static final long serialVersionUID = -3058671297620759261L;
    /**
     * 用户名
     */
    private String username;

    /**
     * 头像地址
     */
    private String path;

}
