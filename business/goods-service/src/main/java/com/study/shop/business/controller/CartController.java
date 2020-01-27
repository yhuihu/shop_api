package com.study.shop.business.controller;

import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.CartService;
import com.study.shop.provider.domain.Cart;
import com.study.shop.provider.dto.CartDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Tiger
 * @date 2020-01-17
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("cart")
public class CartController {
    @Reference(version = "1.0.0")
    private CartService cartService;

    @PostMapping()
    public ResponseResult<Void> addCart(@RequestBody @Valid CartDTO cart) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int flag = cartService.addCart(cart.getList(), authentication.getName());
        if (flag == 1) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK);
        } else {
            throw new BusinessException(ExceptionStatus.REDIS_ERROR);
        }
    }

    @GetMapping()
    public ResponseResult getAllCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Cart> allCart = cartService.getAllCart(authentication.getName());
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, allCart);
    }
}
