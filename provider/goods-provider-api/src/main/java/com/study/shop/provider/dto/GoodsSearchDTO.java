package com.study.shop.provider.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tiger
 * @date 2019-12-15
 * @see com.study.shop.provider.dto
 **/
@Data
public class GoodsSearchDTO implements Serializable {
    private static final long serialVersionUID = 5950902294907735406L;
    /**
     * 页数
     */
    Integer page;
    /**
     * 单页条数
     */
    Integer size;
    /**
     * 排序方式 1价格升序 -1价格降序
     */
    Integer sort;
    /**
     * 最低价格
     */
    Double priceGt;
    /**
     * 最高价格
     */
    Double priceLt;
    /**
     * 关键字
     */
    String keyword;
    /**
     * 分类编号
     */
    Long classificationId;
}
