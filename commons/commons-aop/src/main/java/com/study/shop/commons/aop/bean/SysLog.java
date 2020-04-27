package com.study.shop.commons.aop.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Tiger
 * @date 2020-04-17
 * @see com.study.shop.commons.aop.bean
 **/
@Data
public class SysLog implements Serializable {
    private static final long serialVersionUID = 384814208335371125L;
    private Long id;
    private String name;
    private String url;
    private String requestType;
    private String requestParam;
    private String token;
    private Integer time;
    private String ip;
    private Date createDate;
}
