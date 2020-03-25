package com.study.shop.provider.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Tiger
 * @date 2020-03-23
 * @see com.study.shop.provider.vo
 **/
@Data
public class MessageVO implements Serializable {
    private String fromId;
    private String fromNickName;
    private String fromIcon;
    private String toId;
    private String toNickName;
    private String toIcon;
    private String content;
    private Integer contentType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Integer isRead;
    private Integer isMine;
}
