package com.study.shop.provider.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tiger
 * @date 2020-02-04
 * @see com.study.shop.provider.dto
 **/
@Data
public class AddGoods {
    @NotNull(message = "商品标题不能为空")
    private String title;
    @NotNull(message = "商品卖点不能为空")
    private String sellPoint;
    @NotNull(message = "商品价格不能为空")
    private BigDecimal price;
    @NotNull(message = "商品分类不能为空")
    private String classificationId;
    @NotNull(message = "商品描述不能为空")
    private String desc;
    @NotEmpty(message = "商品预览图片不能为空")
    private List<String>  fileList;
    @NotNull(message = "商品状态不能为空")
    private Integer status;
}
