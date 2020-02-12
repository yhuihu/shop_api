package com.study.shop.provider.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Tiger
 * @date 2020-01-20
 * @see com.study.shop.provider.domain
 **/
@Data
public class Cart implements Serializable {
    private static final long serialVersionUID = 8324453849644218860L;
    @NotBlank(message = "商品编号不能为空")
    private String productId;
    private String productName;
    private String productImg;
    private BigDecimal salePrice;
    private String checked;
}
