package com.study.shop.provider.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Tiger
 * @date 2020-03-20
 * @see com.study.shop.provider.vo
 **/
@Data
public class CheckOrderVO {

    private String id;

    private String goodsId;

    private String groupId;

    private String userId;

    private Date paymentTime;

    private Date consignTime;

    private Date createTime;

    private Integer status;

    private Date endTime;

    private String shippingName;

    private String shippingCode;

    private String buyerMessage;

    private Boolean buyerComment;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;

    private BigDecimal price;

    private String title;

    private String sellerId;
}
