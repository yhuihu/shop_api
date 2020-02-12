package com.study.shop.provider.vo;

import com.study.shop.provider.domain.Cart;
import lombok.Data;

/**
 * @author Tiger
 * @date 2020-02-12
 * @see com.study.shop.provider.vo
 **/
@Data
public class CartVO extends Cart {
    private Integer productNum = 1;
}
