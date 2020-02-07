package com.study.shop.business.controller;

import com.github.pagehelper.PageInfo;
import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.domain.TbItem;
import com.study.shop.provider.dto.AddGoods;
import com.study.shop.provider.dto.GoodsSearchDTO;
import com.study.shop.provider.dto.MyGoodsDTO;
import com.study.shop.provider.dto.UploadGoodsDTO;
import com.study.shop.provider.vo.GoodDetailVO;
import com.study.shop.provider.vo.GoodsVO;
import com.study.shop.utils.SnowIdUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/recommend/{keyword}")
    public ResponseResult<List<String>> getRecommend(@PathVariable String keyword) {
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, tbItemService.searchRecommend(keyword));
    }

    @GetMapping("/search")
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
    @GetMapping()
    public ResponseResult getMyGoods(MyGoodsDTO myGoodsDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        myGoodsDTO.setUsername(username);
        return new ResponseResult(ResponseResult.CodeStatus.OK, tbItemService.getMyGoods(myGoodsDTO));
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/detail/{goodsId}")
    public ResponseResult getMyGoodsDetail(@PathVariable(name = "goodsId") String goodsId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        GoodDetailVO myGoodsDetail = tbItemService.getMyGoodsDetail(username, Long.valueOf(goodsId));
        Map<String, Object> target = new HashMap<>(7);
        target.put("title", myGoodsDetail.getTitle());
        target.put("sellPoint", myGoodsDetail.getSellPoint());
        target.put("price", myGoodsDetail.getPrice());
        target.put("classificationId", String.valueOf(myGoodsDetail.getClassificationId()));
        target.put("desc", myGoodsDetail.getItemDesc());
        List<String> imageList = Arrays.asList(myGoodsDetail.getImage().split(","));
        target.put("fileList", imageList);
        target.put("status", myGoodsDetail.getStatus());
        return new ResponseResult(ResponseResult.CodeStatus.OK, target);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping()
    public ResponseResult deleteMyGoods(@RequestParam(name = "goodsId", required = true) String goodsId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long targetId = Long.valueOf(goodsId);
        int i = tbItemService.deleteGoods(username, targetId);
        if (i > 0) {
            return new ResponseResult(ResponseResult.CodeStatus.OK);
        } else {
            return new ResponseResult(ResponseResult.CodeStatus.FAIL);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping()
    public ResponseResult addGoods(@Valid @RequestBody AddGoods addGoods) {
        TbItem tbItem = getTbItem(addGoods);
        tbItem.setId(SnowIdUtils.uniqueLong());
        int i = tbItemService.addGoods(SecurityContextHolder.getContext().getAuthentication().getName(), tbItem, addGoods.getDesc());
        if (i > 0) {
            return new ResponseResult(ResponseResult.CodeStatus.OK);
        } else {
            return new ResponseResult(ResponseResult.CodeStatus.FAIL);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping()
    public ResponseResult updateGoods(@Valid @RequestBody UploadGoodsDTO uploadGoodsDTO) {
        TbItem tbItem = getTbItem(uploadGoodsDTO);
        tbItem.setId(Long.valueOf(uploadGoodsDTO.getGoodsId()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int i = tbItemService.updateMyGoods(username, tbItem);
        if(i==0){
            return new ResponseResult(ResponseResult.CodeStatus.FAIL);
        }
        return new ResponseResult(ResponseResult.CodeStatus.OK);
    }

    @NotNull
    private TbItem getTbItem(AddGoods addGoods) {
        TbItem tbItem = new TbItem();
        BeanUtils.copyProperties(addGoods, tbItem);
        tbItem.setImage(addGoods.getFileList().toString().replace("[", "").replace("]", "").replace(" ", ""));
        tbItem.setClassificationId(Long.valueOf(addGoods.getClassificationId()));
        return tbItem;
    }
}
