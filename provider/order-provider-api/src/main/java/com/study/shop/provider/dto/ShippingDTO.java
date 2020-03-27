package com.study.shop.provider.dto;

import lombok.Data;

/**
 * @author Tiger
 * @date 2020-03-27
 * @see com.study.shop.provider.dto
 **/
@Data
public class ShippingDTO {
    private String goodsId;
    private String shippingName;
    private String shippingCode;
}
