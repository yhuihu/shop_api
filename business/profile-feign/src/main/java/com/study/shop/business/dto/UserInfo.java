package com.study.shop.business.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business.dto
 **/
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -7077321098372740610L;

    private String id;
    private String username;

    /**
     * 头像
     */
    private String icon;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 余额
     */
    private Double money;

    /**
     * 备注信息
     */
    private String note;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 帐号启用状态：0->禁用；1->启用
     */
    private Integer status;

    private String address;

}
