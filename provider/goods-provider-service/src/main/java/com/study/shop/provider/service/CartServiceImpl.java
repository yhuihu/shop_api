package com.study.shop.provider.service;

import com.study.shop.provider.api.CartService;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.domain.Cart;
import org.apache.dubbo.config.annotation.Reference;

import java.util.List;

/**
 * @author Tiger
 * @date 2020-01-21
 * @see com.study.shop.provider.service
 **/
public class CartServiceImpl implements CartService {
    @Reference(version = "1.0.0")
    private TbItemService tbItemService;


    @Override
    public int addCart(List<Cart> list) {
        return 0;
    }
}
