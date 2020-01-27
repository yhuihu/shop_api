package com.study.shop.provider.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Tiger
 * @date 2020-01-20
 * @see com.study.shop.provider.domain
 **/
@Data
public class Cart implements Serializable {
    //这是一个表？不是 那你放domain里面干嘛 持久化到redis嘛
    private static final long serialVersionUID = 8324453849644218860L;
    @NotBlank(message = "商品编号不能为空")
    private String productId;
    @NotNull(message = "商品数量不能为空")
    private Integer productNum;
    private String productImg;
}
