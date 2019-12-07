package com.study.shop.provider.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb_item")
public class TbItem implements Serializable {
    /**
     * 商品id，同时也是商品编号
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 商品标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 商品卖点
     */
    @Column(name = "sell_point")
    private String sellPoint;

    /**
     * 商品价格
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 商品图片
     */
    @Column(name = "image")
    private String image;

    /**
     * 所属分类
     */
    @Column(name = "cid")
    private Long cid;

    /**
     * 商品状态 1正常 0下架
     */
    @Column(name = "`status`")
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "created")
    private Date created;

    /**
     * 更新时间
     */
    @Column(name = "updated")
    private Date updated;

    private static final long serialVersionUID = 1L;
}