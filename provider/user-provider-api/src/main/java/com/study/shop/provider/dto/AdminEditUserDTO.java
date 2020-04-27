package com.study.shop.provider.dto;

import com.study.shop.provider.validation.Phone;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author Tiger
 * @date 2020-04-18
 * @see com.study.shop.provider.dto
 **/
@Data
public class AdminEditUserDTO {
    @NotNull(message = "用户编号错误")
    private String userId;
    @Phone
    private String phone;
    @Email(message = "邮箱错误")
    private String email;
    @NotNull(message = "状态错误")
    private Integer status;
}
