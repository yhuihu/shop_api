package com.study.shop.provider.vo;

import lombok.Data;

/**
 * @author Tiger
 * @date 2020-04-20
 * @see com.study.shop.provider.vo
 **/
@Data
public class TbClassificationVO {
    private String id;
    private String parentId;
    private String name;
    private Integer status;
    private Integer sortOrder;
    private Boolean isParent;
    private String remark;
}
