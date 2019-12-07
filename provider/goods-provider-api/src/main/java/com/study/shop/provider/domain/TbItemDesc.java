package com.study.shop.provider.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tb_item_desc")
public class TbItemDesc implements Serializable {
    /**
     * 商品ID
     */
    @Id
    @Column(name = "item_id")
    private Long itemId;

    /**
     * 商品描述
     */
    @Column(name = "item_desc")
    private String itemDesc;

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
