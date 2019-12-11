package com.study.shop.business.controller;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbItemService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseResult searchGoods(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                      @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                      @RequestParam(name = "sort", required = false, defaultValue = "0") Integer sort,
                                      @RequestParam(name = "priceGt", required = false, defaultValue = "0") Double priceGt,
                                      @RequestParam(name = "priceLt", required = false, defaultValue = "0") Double priceLt,
                                      @RequestParam(name = "keyword", required = false) String keyword) {
        return new ResponseResult(ResponseResult.CodeStatus.OK, tbItemService.searchItem(page, size, sort, priceGt, priceLt, keyword));
    }
}
