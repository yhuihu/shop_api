package com.study.shop.provider.domain;

import lombok.Data;

import java.util.List;

/**
 * @author Tiger
 * @date 2020-01-20
 * @see com.study.shop.provider.domain
 **/
@Data
public class Cart {
    private List<CartData> list;

    @Data
    public static class CartData {
        private String productId;
        private Integer productNum;
    }
}
