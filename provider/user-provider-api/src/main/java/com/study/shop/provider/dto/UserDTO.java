package com.study.shop.provider.dto;

import com.study.shop.provider.validation.Phone;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author Tiger
 * @date 2020-03-14
 * @see com.study.shop.provider.dto
 **/
@Data
public class UserDTO {
    @NotNull
    private String username;

    @NotNull
    private String password;

    /**
     * 邮箱
     */
    @Email
    private String email;

    /**
     * 所在地
     */
    @NotNull
    private String code;

    /**
     * 昵称
     */
    @NotNull
    private String nickName;

    /**
     * 手机号
     */
    @Phone
    private String phone;

    /**
     * 所在地
     */
    @NotNull
    private String address;
}
