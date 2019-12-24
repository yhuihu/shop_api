package com.study.shop.business.controller;

import com.github.pagehelper.PageInfo;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.dto.GoodsSearchDTO;
import com.study.shop.provider.vo.GoodsVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Tiger
 * @date 2019-12-06
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("goods")
public class GoodsSearchController {
    @Reference(version = "1.0.0")
    private TbItemService tbItemService;

    @GetMapping("recommend/{keyword}")
    public ResponseResult<List<String>> getRecommend(@PathVariable String keyword) {
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, tbItemService.searchRecommend(keyword));
    }

    @GetMapping()
    public ResponseResult<PageInfo<GoodsVO>> searchGoods(GoodsSearchDTO goodsSearchDTO) {
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, tbItemService.searchItem(goodsSearchDTO));
    }
}
