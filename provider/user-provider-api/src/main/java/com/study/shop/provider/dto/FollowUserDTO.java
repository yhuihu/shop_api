package com.study.shop.provider.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Tiger
 * @date 2020-03-22
 * @see com.study.shop.provider.dto
 **/
@Data
public class FollowUserDTO {
    private String userId;
    private String nickName;
    private String icon;
    private Date createTime;
}
