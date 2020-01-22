package com.study.shop.business.controller;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.domain.Cart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tiger
 * @date 2020-01-17
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("cart")
public class CartController {
    @PostMapping()
    public ResponseResult addCart(@RequestBody Cart cart) {
        System.out.println(cart);
        return new ResponseResult<>();
    }

    @GetMapping()
    public ResponseResult getAllCart(){
        return new ResponseResult<>();
    }
}
