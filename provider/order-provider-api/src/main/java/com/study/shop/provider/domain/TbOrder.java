package com.study.shop.provider.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tb_order")
public class TbOrder implements Serializable {
    /**
     * 订单id
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 支付类型 1在线支付 2货到付款
     */
    @Column(name = "pay_type")
    private Integer payType;

    /**
     * 实付金额
     */
    @Column(name = "payment")
    private Double payment;

    /**
     * 商品编号
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 同次下单商品有相同的group，便于发生错误时可以实现分布式事务
     */
    @Column(name = "group_id")
    private Long groupId;

    /**
     * 状态 0未付款 1已付款 2未发货 3已发货 4交易成功 5交易关闭
     */
    @Column(name = "`status`")
    private Integer status;

    /**
     * 订单创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 付款时间
     */
    @Column(name = "payment_time")
    private Date paymentTime;

    /**
     * 发货时间
     */
    @Column(name = "consign_time")
    private Date consignTime;

    /**
     * 交易完成时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 物流名称
     */
    @Column(name = "shipping_name")
    private String shippingName;

    /**
     * 物流单号
     */
    @Column(name = "shipping_code")
    private String shippingCode;

    /**
     * 买家留言
     */
    @Column(name = "buyer_message")
    private String buyerMessage;

    /**
     * 买家是否已经评价
     */
    @Column(name = "buyer_comment")
    private Boolean buyerComment;

    /**
     * 买家收货人姓名
     */
    @Column(name = "buyer_name")
    private String buyerName;

    /**
     * 买家收货手机号
     */
    @Column(name = "buyer_phone")
    private String buyerPhone;

    /**
     * 买家收货地址
     */
    @Column(name = "buyer_address")
    private String buyerAddress;

    private static final long serialVersionUID = 1L;
}
