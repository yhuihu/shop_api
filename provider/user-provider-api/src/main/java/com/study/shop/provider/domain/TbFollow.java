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
@Table(name = "tb_follow")
public class TbFollow implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 所属用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 目标用户id
     */
    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
