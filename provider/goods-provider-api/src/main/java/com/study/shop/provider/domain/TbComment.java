package com.study.shop.provider.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author admin
 */
@Data
@Table(name = "tb_comment")
public class TbComment implements Serializable {
    /**
     * 评论编号
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 商品编号
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 评论内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 用户编号
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 回复编号,0代表不是回复
     */
    @Column(name = "reply_id")
    private Long replyId;

    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
