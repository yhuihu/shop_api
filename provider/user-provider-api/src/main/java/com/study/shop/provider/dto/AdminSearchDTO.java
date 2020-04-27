package com.study.shop.provider.dto;

import lombok.Data;

/**
 * @author Tiger
 * @date 2020-04-17
 * @see com.study.shop.provider.dto
 **/
@Data
public class AdminSearchDTO {
    private Integer page;
    private Integer size;
    private Integer status;
    private String keyword;
    private String startTime;
    private String endTime;
}
