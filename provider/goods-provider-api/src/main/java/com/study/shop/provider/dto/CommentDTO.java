package com.study.shop.provider.dto;

import lombok.Data;

/**
 * @author Tiger
 * @date 2020-03-18
 * @see com.study.shop.provider.dto
 **/
@Data
public class CommentDTO {

    /**
     * 商品编号
     */
    private String goodsId;
    /**
     * 被回复者用户名
     */
    private String replyUser;
    /**
     * 内容
     */
    private String content;
    /**
     * 被回复的评论id
     */
    private String replyId;

}
