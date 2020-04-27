package com.study.shop.provider.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Tiger
 * @date 2019-12-16
 * @see com.study.shop.provider.vo
 **/
@Data
public class GoodsVO {
    private String id;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品卖点
     */
    private String sellPoint;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 所属分类
     */
    private Long classificationId;

    /**
     * 商品状态 1正常 2已售出 0下架
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date updated;

    /**
     * 商品对应用户
     */
    private String userId;

    private String buyerId;

    /**
     * （这里需要注意，在搜索商品的地方获取的是卖家的信息。在用户后台管理闲置物品中获取的是买家的信息）
     */
    private String nickName;

    private String icon;

    private String address;
}
