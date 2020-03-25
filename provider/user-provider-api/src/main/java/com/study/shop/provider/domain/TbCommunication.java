package com.study.shop.provider.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tb_communication")
public class TbCommunication implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 0代表字符串，1代表图片
     */
    @Column(name = "content_type")
    private Integer contentType;

    @Column(name = "content")
    private String content;

    /**
     * 发送用户，为空时代表系统消息
     */
    @Column(name = "from_id")
    private Long fromId;

    /**
     * 接收用户
     */
    @Column(name = "to_id")
    private Long toId;

    /**
     * 0未读，1已读
     */
    @Column(name = "is_read")
    private Integer isRead;

    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
