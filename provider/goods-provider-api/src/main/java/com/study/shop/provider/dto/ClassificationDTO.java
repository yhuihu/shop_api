package com.study.shop.provider.dto;

import lombok.Data;

/**
 * @author Tiger
 * @date 2020-04-27
 * @see com.study.shop.provider.dto
 **/
@Data
public class ClassificationDTO {

    private String id;

    private String parentId;

    private String name;

    private String status;

    private Integer sortOrder;

    private String isParent;

    private String remark;
}
