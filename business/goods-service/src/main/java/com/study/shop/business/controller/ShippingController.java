package com.study.shop.business.controller;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.ShippingService;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.dto.ShippingDTO;
import com.study.shop.provider.vo.GoodDetailVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping()
    public ResponseResult postShipping(@RequestBody ShippingDTO shippingDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        GoodDetailVO myGoodsDetail = tbItemService.getMyGoodsDetail(tbUser.getId(), Long.valueOf(shippingDTO.getGoodsId()));
        if (myGoodsDetail != null) {
            int i = shippingService.postSkipping(shippingDTO, tbUser.getId());
            if (i > 0) {
                List<Long> goodsId = new ArrayList<>();
                goodsId.add(Long.valueOf(shippingDTO.getGoodsId()));
                tbItemService.changeGoodsStatus(goodsId, 6);
                return new ResponseResult(ResponseResult.CodeStatus.OK);
            }
        }
        return new ResponseResult(ResponseResult.CodeStatus.FAIL, "请稍后重试");
    }
}
