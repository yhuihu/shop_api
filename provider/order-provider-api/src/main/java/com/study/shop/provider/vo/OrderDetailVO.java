package com.study.shop.provider.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Tiger
 * @date 2020-03-27
 * @see com.study.shop.provider.vo
 **/
@Data
public class OrderDetailVO extends OrderListVO {
    private String sellerId;
    private String sellerIcon;
    private String sellerName;
    private BigDecimal sellPrice;
}
