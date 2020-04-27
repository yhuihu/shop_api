package com.study.shop.business.controller;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.ShippingService;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.api.TbOrderService;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.dto.ShippingDTO;
import com.study.shop.provider.vo.GoodDetailVO;
import com.study.shop.provider.vo.OrderDetailVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tiger
 * @date 2020-03-27
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("shipping")
public class ShippingController {
    @Reference(version = "1.0.0")
    private TbUserService tbUserService;
    @Reference(version = "1.0.0")
    private ShippingService shippingService;
    @Reference(version = "1.0.0")
    private TbItemService tbItemService;
    @Reference(version = "1.0.0")
    private TbOrderService tbOrderService;

    /**
     * 发货
     *
     * @param shippingDTO s
     * @return result
     */
    @PostMapping()
    public ResponseResult postShipping(@RequestBody ShippingDTO shippingDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        // 先判断该商品是否是自己的
        GoodDetailVO myGoodsDetail = tbItemService.getMyGoodsDetail(tbUser.getId(), Long.valueOf(shippingDTO.getGoodsId()));
        if (myGoodsDetail != null) {
            // 保存信息
            int i = shippingService.postSkipping(shippingDTO, tbUser.getId());
            if (i > 0) {
                List<Long> goodsId = new ArrayList<>();
                goodsId.add(Long.valueOf(shippingDTO.getGoodsId()));
                // 修改商品状态为已发货
                tbItemService.changeGoodsStatus(goodsId, 6);
                return new ResponseResult(ResponseResult.CodeStatus.OK);
            }
        }
        return new ResponseResult(ResponseResult.CodeStatus.FAIL, "请稍后重试");
    }

    /**
     * 确认收货
     *
     * @param id 订单编号
     * @return result
     */
    @PutMapping("/{id}")
    public ResponseResult checkShipping(@PathVariable(name = "id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        OrderDetailVO orderDetail = tbOrderService.getOrderDetail(Long.valueOf(id));
        if (!Long.valueOf(orderDetail.getUserId()).equals(tbUser.getId())) {
            return new ResponseResult(ResponseResult.CodeStatus.FAIL, "不存在该订单");
        }
        if (orderDetail == null) {
            return new ResponseResult(ResponseResult.CodeStatus.FAIL, "不存在该订单");
        }
        // 商品状态改为2
        List<Long> goodsId = new ArrayList<>();
        goodsId.add(Long.valueOf(orderDetail.getGoodsId()));
        tbItemService.changeGoodsStatus(goodsId, 2);
        // 订单状态改为4
        tbOrderService.changeOrderStatusByOrderId(Long.valueOf(orderDetail.getId()), 4);
        return new ResponseResult(ResponseResult.CodeStatus.OK);
    }
}
