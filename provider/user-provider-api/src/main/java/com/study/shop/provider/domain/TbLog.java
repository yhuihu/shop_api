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
@Table(name = "tb_log")
public class TbLog implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "`name`")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "request_param")
    private String requestParam;

    @Column(name = "`user`")
    private String user;

    @Column(name = "ip")
    private String ip;

    @Column(name = "`time`")
    private Integer time;

    @Column(name = "create_date")
    private Date createDate;

    private static final long serialVersionUID = 1L;
}
