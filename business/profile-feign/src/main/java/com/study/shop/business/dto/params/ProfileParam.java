package com.study.shop.business.dto.params;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.business.dto.params
 **/
@Data
public class ProfileParam implements Serializable {
    private static final long serialVersionUID = -7053678588058588491L;
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
     * 备注
     */
    private String note;
}
