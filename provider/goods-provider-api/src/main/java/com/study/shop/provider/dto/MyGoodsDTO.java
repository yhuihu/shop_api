package com.study.shop.provider.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tiger
 * @date 2020-02-06
 * @see com.study.shop.provider.dto
 **/
@Data
public class MyGoodsDTO implements Serializable {
    private static final long serialVersionUID = -5931005041982292635L;
    /**
     * 页数
     */
    Integer page;
    /**
     * 单页条数
     */
    Integer size;
    /**
     * 状态
     */
    Integer status;
    /**
     * 关键字
     */
    String keyword;
    /**
     * 用户编号
     */
    Long userId;
}
