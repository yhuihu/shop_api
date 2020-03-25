package com.study.shop.provider.api;

import com.study.shop.provider.domain.TbOrder;
import com.study.shop.provider.vo.CheckOrderVO;

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
     * @return 0失败,1成功
     */
    int addOrder(TbOrder tbOrder);

    /**
     * 确认订单
     * @param userId 用户编号
     * @param groupId 订单组号
     * @return 0失败，1成功
     */
    int checkOrder(Long userId,Long groupId);

    /**
     * 确认订单时需要获取的订单内容
     * @param groupId groupId
     * @param userId userId
     * @return {@link CheckOrderVO}
     */
    List<CheckOrderVO> getByGroupCheck(Long groupId,Long userId);

    /**
     * 根据group获取当前group对应的商品
     * @param groupId groupId
     * @return List<TbOrder>
     */
    List<TbOrder> getByGroup(Long groupId);

    /**
     * 根据groupId删除订单
     * @param groupId groupId
     * @return int
     */
    int deleteByGroup(Long groupId);

    /**
     * 获取过期订单group
     * @param date date
     * @return list
     */
    List<TbOrder> getTimeOutOrder(Date date);

    /**
     * 修改订单状态
     * @param groupId groupId
     * @return 0,1
     */
    int changeOrderStatus(Long groupId,Integer status);
}


