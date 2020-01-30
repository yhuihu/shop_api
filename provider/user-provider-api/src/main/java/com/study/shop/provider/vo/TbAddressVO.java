package com.study.shop.provider.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tiger
 * @date 2020-01-30
 * @see com.study.shop.provider.vo
 **/
@Data
public class TbAddressVO implements Serializable {
    private static final long serialVersionUID = -6529174957754073102L;

    private String addressId;

    private String userId;

    private String tel;

    private String streetName;

    private Boolean isDefault;
}
