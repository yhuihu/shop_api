package com.study.shop.business.util;

import lombok.Data;

/**
 * @author Tiger
 * @date 2020-03-19
 * @see com.study.shop.business.util
 **/
@Data
public class OrderDetail {
    private String goodsId;
    private Integer payType;
}
