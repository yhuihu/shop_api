package com.study.shop.provider.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Tiger
 * @date 2020-03-24
 * @see com.study.shop.provider.dto
 **/
@Data
public class CommunicationDetailDTO {
    private String icon;
    private String content;
    private Integer contentType;
    private Date createTime;
    private Integer isMine;
}
