package com.study.shop.business.controller;

import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.CartService;
import com.study.shop.provider.domain.Cart;
import com.study.shop.provider.dto.CartDTO;
import com.study.shop.provider.vo.CartVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        int flag = cartService.addCart(cart.getList(), getUser());
        if (flag == 1) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK);
        } else {
            throw new BusinessException(ExceptionStatus.REDIS_ERROR);
        }
    }

    @GetMapping()
    public ResponseResult getAllCart() throws ExecutionException, InterruptedException {
        List<Cart> allCart = cartService.getAllCart(getUser());
        List<CartVO> answer = new ArrayList<>(allCart.size());
        allCart.forEach(item -> {
            CartVO cartVO = new CartVO();
            BeanUtils.copyProperties(item, cartVO);
            answer.add(cartVO);
        });
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, answer);
    }

    @DeleteMapping("/{productId}")
    public ResponseResult deleteCart(@PathVariable(name = "productId") String target) {
        String username = getUser();
        int i = cartService.deleteCart(username, target);
        if (i > 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK);
        } else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL);
        }
    }

    @DeleteMapping()
    public ResponseResult deleteCheck() {
        String username = getUser();
        int i = cartService.deleteCheck(username);
        if (i > 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK);
        } else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL);
        }
    }

    @PostMapping("/{check}")
    public ResponseResult checkAll(@PathVariable(name = "check") String check) {
        String username = getUser();
        int i = cartService.allCheck(username, check);
        if (i > 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK);
        } else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL);
        }
    }

    @PutMapping("/{check}/{productId}")
    public ResponseResult changeCheck(@PathVariable(name = "check") String check, @PathVariable(name = "productId") String productId) {
        String username = getUser();
        int i = cartService.changeCheck(username, productId, check);
        if (i > 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK);
        } else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL);
        }
    }

    private String getUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
