package com.study.shop.provider.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tiger
 * @date 2019-12-24
 * @see com.study.shop.provider.vo
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ClassificationVO implements Serializable {

    private static final long serialVersionUID = -2609386651682218049L;

    private String value;

    private String label;

    List<ClassificationVO> children;
}
