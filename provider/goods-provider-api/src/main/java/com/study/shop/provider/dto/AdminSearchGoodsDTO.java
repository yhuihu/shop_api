package com.study.shop.provider.dto;

import lombok.Data;

/**
 * @author Tiger
 * @date 2020-04-18
 * @see com.study.shop.provider.dto
 **/
@Data
public class AdminSearchGoodsDTO {
    private String keyword;
    private Integer page;
    private Integer size;
    private Integer status;
    private String cid;
    private String startTime;
    private String endTime;
}
