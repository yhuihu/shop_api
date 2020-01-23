package com.study.shop.provider.api;

import com.study.shop.provider.domain.Cart;

import java.util.List;

/**
 * @author Tiger
 * @date 2020-01-20
 * @see com.study.shop.provider.api
 **/
public interface CartService {
    /**
     * 功能描述: <br>
     * @param list 数据集
     * @return 0 1
     */
    int addCart(List<Cart> list);
}
