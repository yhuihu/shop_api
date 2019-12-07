package com.study.shop.business.controller;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbItemService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tiger
 * @date 2019-12-06
 * @see com.study.shop.business.controller
 **/
@RestController
public class GoodsSearchController {
    @Reference(version = "1.0.0")
    private TbItemService tbItemService;

    @GetMapping("recommend/{keyword}")
    public ResponseResult getRecommend(@PathVariable String keyword){
        tbItemService.searchRecommend(keyword);
        return new ResponseResult(200,tbItemService.searchRecommend(keyword));
    }
}
