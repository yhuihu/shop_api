package com.study.shop.provider.dto;

import com.study.shop.provider.domain.Cart;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @author Tiger
 * @date 2020-01-27
 * @see com.study.shop.provider.dto
 **/
@Data
public class CartDTO implements Serializable {
    private static final long serialVersionUID = 6063109146000147179L;
    @Valid
    private List<Cart> list;
}
