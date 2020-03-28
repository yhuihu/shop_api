package com.study.shop.provider.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Tiger
 * @date 2020-03-26
 * @see com.study.shop.provider.dto
 **/
@Data
public class OrderListVO {
    /**
     * 订单id
     */
    private String id;

    /**
     * 支付类型 1在线支付 2货到付款
     */
    private Integer payType;

    /**
     * 实付金额
     */
    private Double payment;

    /**
     * 商品编号
     */
    private String goodsId;

    /**
     * 同次下单商品有相同的group，便于发生错误时可以实现分布式事务
     */
    private String groupId;

    /**
     * 状态 0未付款 1已付款 2未发货 3已发货 4交易成功 5交易关闭
     */
    private Integer status;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 付款时间
     */
    private Date paymentTime;

    /**
     * 发货时间
     */
    private Date consignTime;

    /**
     * 交易完成时间
     */
    private Date endTime;

    /**
     * 物流名称
     */
    private String shippingName;

    /**
     * 物流单号
     */
    private String shippingCode;

    /**
     * 买家留言
     */
    private String buyerMessage;

    /**
     * 买家是否已经评价
     */
    private Boolean buyerComment;

    /**
     * 买家收货人姓名
     */
    private String buyerName;

    /**
     * 买家收货手机号
     */
    private String buyerPhone;

    /**
     * 买家收货地址
     */
    private String buyerAddress;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 标题
     */
    private String title;

    /**
     * 售价
     */
    private BigDecimal sellPrice;
}
