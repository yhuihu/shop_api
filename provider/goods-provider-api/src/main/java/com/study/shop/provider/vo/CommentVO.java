package com.study.shop.provider.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Tiger
 * @date 2020-03-18
 * @see com.study.shop.provider.vo
 **/
@Data
public class CommentVO {
    private static final long serialVersionUID = -2743690539728573516L;
    private String id;
    private String goodsId;
    private String userId;
    private Long replyId;
    private Date createTime;
    private String content;
    private String nickName;
    private String icon;
    private Integer isMine;
    List<CommentVO> children;
}
