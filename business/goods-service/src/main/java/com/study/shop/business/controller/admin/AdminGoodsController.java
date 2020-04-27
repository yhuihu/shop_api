package com.study.shop.business.controller.admin;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.dto.AdminSearchGoodsDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tiger
 * @date 2020-04-18
 * @see com.study.shop.business.controller.admin
 **/
@RestController
@RequestMapping("admin-goods")
public class AdminGoodsController {
    @Reference(version = "1.0.0")
    TbItemService tbItemService;

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseResult adminGetGoodsList(AdminSearchGoodsDTO adminSearchGoodsDTO) {
        return new ResponseResult(ResponseResult.CodeStatus.OK, tbItemService.adminGetGoodsListByPage(adminSearchGoodsDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseResult adminDeleteGoods(@PathVariable(name = "id") String id) {
        return new ResponseResult(ResponseResult.CodeStatus.OK,tbItemService.adminDeleteGoods(Long.valueOf(id)));
    }
}
