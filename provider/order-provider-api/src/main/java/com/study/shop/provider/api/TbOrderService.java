package com.study.shop.provider.api;

import com.github.pagehelper.PageInfo;
import com.study.shop.provider.domain.TbOrder;
import com.study.shop.provider.dto.OrderListDTO;
import com.study.shop.provider.vo.CheckOrderVO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 */
public interface TbOrderService {

    /**
     * 添加订单
     *
     * @param tbOrder {@link TbOrder}
     * @return 0失败, 1成功
     */
    int addOrder(TbOrder tbOrder);

    /**
     * 确认订单
     *
     * @param userId        用户编号
     * @param groupId       订单组号
     * @param checkOrderVOS 将商品价格插入
     * @return 0失败，1成功
     */
    int checkOrder(Long userId, Long groupId, List<CheckOrderVO> checkOrderVOS);

    /**
     * 确认订单时需要获取的订单内容
     *
     * @param groupId groupId
     * @param userId  userId
     * @return {@link CheckOrderVO}
     */
    List<CheckOrderVO> getByGroupCheck(Long groupId, Long userId);

    /**
     * 根据group获取当前group对应的商品
     *
     * @param groupId groupId
     * @return List<TbOrder>
     */
    List<TbOrder> getByGroup(Long groupId);

    /**
     * 根据groupId删除订单
     *
     * @param groupId groupId
     * @return int
     */
    int deleteByGroup(Long groupId);

    /**
     * 获取过期订单group
     *
     * @param date date
     * @return list
     */
    List<TbOrder> getTimeOutOrder(Date date);

    /**
     * 修改订单状态
     * @param status 状态
     * @param groupId groupId
     * @return 0, 1
     */
    int changeOrderStatus(Long groupId, Integer status);

    /**
     * 获取我的订单
     *
     * @param userId 用户id
     * @param page   页码
     * @param size   大小
     * @return {@link PageInfo}
     */
    PageInfo<OrderListDTO> getMyOrder(Long userId, Integer page, Integer size);

    /**
     * 支付订单
     * @param orderId 订单编号
     * @param userId 用户编号
     * @param goodsId 商品编号
     * @param price 价格
     * @return 0,1
     */
    int payOrder(Long orderId, Long userId, Long goodsId, BigDecimal price);

    /**
     * 删除订单
     * @param orderId 订单编号
     * @param userId 用户编号
     * @return 0,1
     */
    int deleteOrder(Long orderId,Long userId);
}


