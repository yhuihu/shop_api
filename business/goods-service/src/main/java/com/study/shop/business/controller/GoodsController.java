package com.study.shop.business.controller;

import com.github.pagehelper.PageInfo;
import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.domain.TbItem;
import com.study.shop.provider.dto.AddGoods;
import com.study.shop.provider.dto.GoodsSearchDTO;
import com.study.shop.provider.vo.GoodDetailVO;
import com.study.shop.provider.vo.GoodsVO;
import com.study.shop.utils.SnowIdUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Tiger
 * @date 2019-12-06
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("goods")
public class GoodsController {
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

    @GetMapping("/{goodsId}")
    public ResponseResult<GoodDetailVO> goodsDetail(@PathVariable("goodsId") String goodsId) {
        if (!StringUtils.isNumeric(goodsId)) {
            throw new BusinessException(ExceptionStatus.ILLEGAL_REQUEST);
        }
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取商品详情", tbItemService.getGoodDetail(Long.valueOf(goodsId)));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping()
    public ResponseResult addGoods(@Valid @RequestBody AddGoods addGoods) {
        TbItem tbItem = getTbItem(addGoods);
        tbItem.setId(SnowIdUtils.uniqueLong());
        int i = tbItemService.addGoods(SecurityContextHolder.getContext().getAuthentication().getName(), tbItem, addGoods.getDesc());
        return new ResponseResult(ResponseResult.CodeStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping()
    public ResponseResult updateGoods(@Valid @RequestBody AddGoods addGoods) {
        TbItem tbItem = getTbItem(addGoods);
        int i = tbItemService.addGoods(SecurityContextHolder.getContext().getAuthentication().getName(), tbItem, addGoods.getDesc());
        return new ResponseResult(ResponseResult.CodeStatus.OK);
    }

    @NotNull
    private TbItem getTbItem(AddGoods addGoods) {
        TbItem tbItem=new TbItem();
        BeanUtils.copyProperties(addGoods, tbItem);
        tbItem.setImage(addGoods.getFileList().toString().replace("[", "").replace("]", "").replace(" ", ""));
        tbItem.setClassificationId(Long.valueOf(addGoods.getClassificationId()));
        return tbItem;
    }
}
