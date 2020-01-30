package com.study.shop.provider.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "tb_address")
public class TbAddress implements Serializable {
    @Id
    @Column(name = "address_id")
    @GeneratedValue(generator = "JDBC")
    private Long addressId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "tel")
    private String tel;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "is_default")
    private Boolean isDefault;

    private static final long serialVersionUID = 1L;
}
