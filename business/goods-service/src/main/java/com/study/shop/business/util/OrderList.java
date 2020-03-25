package com.study.shop.business.util;

import lombok.Data;

import java.util.List;

/**
 * @author Tiger
 * @date 2020-03-12
 * @see com.study.shop.business.util
 **/
@Data
public class OrderList {
    private List<OrderDetail> orderDetails;
    private String buyerPhone;
    private String buyerName;
    private String buyerAddress;
}
