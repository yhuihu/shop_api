package com.study.shop.provider.dto;

import lombok.Data;

/**
 * @author Tiger
 * @date 2020-03-17
 * @see com.study.shop.provider.dto
 **/
@Data
public class FindDTO {
    /**
     * 邮箱
     */
    private String email;
    /**
     * 验证码
     */
    private String code;
    /**
     * 密码
     */
    private String password;
}
