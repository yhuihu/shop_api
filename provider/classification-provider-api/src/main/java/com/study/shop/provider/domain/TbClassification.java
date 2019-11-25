package com.study.shop.provider.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author admin
 */
@Data
@Table(name = "tb_classification")
public class TbClassification implements Serializable {
    /**
     * 类目ID
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 父分类ID=0时代表一级根分类
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 分类名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 状态 1启用 0禁用
     */
    @Column(name = "`status`")
    private Integer status;

    /**
     * 排列序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder;

    /**
     * 是否为父分类 1为true 0为false
     */
    @Column(name = "is_parent")
    private Boolean isParent;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    private static final long serialVersionUID = 1L;
}
