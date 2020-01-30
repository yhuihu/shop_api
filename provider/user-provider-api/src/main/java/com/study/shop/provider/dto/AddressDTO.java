package com.study.shop.provider.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Tiger
 * @NotEmpty 用在集合类上面
 * @NotBlank 用在String上面
 * @NotNull    用在基本类型上
 * @date 2020-01-29
 * @see com.study.shop.provider.dto
 **/
@Data
public class AddressDTO {
    private Long addressId;
    @NotBlank(message = "手机号错误")
    private String tel;
    @NotBlank(message = "地址不能为空")
    private String streetName;
    @NotNull(message = "默认值参数错误")
    private Boolean isDefault;
}
