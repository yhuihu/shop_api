package com.study.shop.provider.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Tiger
 * @date 2020-02-07
 * @see com.study.shop.provider.dto
 **/
@Data
public class UploadGoodsDTO extends AddGoods {
    @NotNull(message = "商品编号不能为空")
    private String goodsId;
}
