package com.study.shop.business.controller;

import com.github.pagehelper.PageInfo;
import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbItem;
import com.study.shop.provider.domain.TbUser;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    @Reference(version = "1.0.0")
    private TbUserService tbUserService;
    @Autowired
    RedisTemplate redisTemplate;

    private String ACCESS_LOG_KEY = "user_logs_";

    /**
     * 搜索推荐接口
     *
     * @param keyword 关键字
     * @return 推荐列表
     */
    @GetMapping("/recommend/{keyword}")
    public ResponseResult<List<String>> getRecommend(@PathVariable String keyword) {
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, tbItemService.searchRecommend(keyword));
    }

    /**
     * 搜索商品
     *
     * @param goodsSearchDTO 条件实体
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseResult<PageInfo<GoodsVO>> searchGoods(GoodsSearchDTO goodsSearchDTO) {
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, tbItemService.searchItem(goodsSearchDTO));
    }

    /**
     * 获取商品详情
     *
     * @param goodsId goodsId
     * @return 详情实体
     */
    @GetMapping("/{goodsId}")
    public ResponseResult<Object> goodsDetail(@PathVariable("goodsId") String goodsId) {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (!"anonymousUser".equals(username)) {
            //        在redis中保存用户足迹
            zSetOperations.add(ACCESS_LOG_KEY + username, goodsId, System.currentTimeMillis());
        }
        if (!StringUtils.isNumeric(goodsId)) {
            throw new BusinessException(ExceptionStatus.ILLEGAL_REQUEST);
        }
        GoodDetailVO goodDetail = tbItemService.getGoodDetail(Long.valueOf(goodsId));
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取商品详情", goodDetail);
    }

    @GetMapping("/logs")
    public ResponseResult<Object> getMyLogs(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                            @RequestParam(name = "size", defaultValue = "8") Integer size) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int totalPage = 0;
        int totalCount = 0;
        List<GoodDetailVO> logsDetail = new ArrayList<>();
        Set<String> logSet = new HashSet<>();
        if (!"anonymousUser".equals(username)) {
            //        在redis中获取用户足迹   精度丢失问题，先转为double
            Double redisCount = zSetOperations.size(ACCESS_LOG_KEY + username).doubleValue();
            double i = redisCount / size;
            double redisPage = Math.ceil(i);
            totalCount = redisCount.intValue();
            totalPage = (int) redisPage;
            logSet = zSetOperations.reverseRange(ACCESS_LOG_KEY + username, (page - 1) * size, page * size - 1);
        }
        if (logSet != null) {
            List<Long> longList = new ArrayList<>();
            for (String s : logSet) {
                longList.add(Long.valueOf(s));
            }
            logsDetail = tbItemService.getLogsGoodsDetail(longList);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("logs", logsDetail);
        map.put("totalCount", totalCount);
        map.put("totalPage", totalPage);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取商品详情", map);
    }

    /**
     * 获取我发布的商品
     *
     * @param myGoodsDTO myGoods
     * @return 实体
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping()
    public ResponseResult getMyGoods(MyGoodsDTO myGoodsDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        if (tbUser == null) {
            throw new BusinessException(ExceptionStatus.ACCOUNT_NOT_EXIST);
        }
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, tbItemService.getMyGoods(myGoodsDTO, tbUser.getId()));
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/detail/{goodsId}")
    public ResponseResult getMyGoodsDetail(@PathVariable(name = "goodsId") String goodsId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        if (tbUser == null) {
            throw new BusinessException(ExceptionStatus.ACCOUNT_NOT_EXIST);
        }
        GoodDetailVO myGoodsDetail = tbItemService.getMyGoodsDetail(tbUser.getId(), Long.valueOf(goodsId));
        Map<String, Object> target = new HashMap<>(7, 1);
        target.put("title", myGoodsDetail.getTitle());
        target.put("sellPoint", myGoodsDetail.getSellPoint());
        target.put("price", myGoodsDetail.getPrice());
        target.put("classificationId", String.valueOf(myGoodsDetail.getClassificationId()));
        target.put("desc", myGoodsDetail.getItemDesc());
        List<String> imageList = Arrays.asList(myGoodsDetail.getImage().split(","));
        target.put("fileList", imageList);
        target.put("status", myGoodsDetail.getStatus());
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, target);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping()
    public ResponseResult deleteMyGoods(@RequestParam(name = "goodsId", required = true) String goodsId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        if (tbUser == null) {
            throw new BusinessException(ExceptionStatus.ACCOUNT_NOT_EXIST);
        }
        Long targetId = Long.valueOf(goodsId);
        GoodDetailVO goodDetail = tbItemService.getGoodDetail(Long.valueOf(goodsId));
        if (goodDetail.getStatus() == 2 || goodDetail.getStatus() == 3) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "已售出或被拍下商品不可删除！");
        }
        int i = tbItemService.deleteGoods(tbUser.getId(), targetId);
        if (i > 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK);
        } else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping()
    public ResponseResult addGoods(@Valid @RequestBody AddGoods addGoods) {
        TbItem tbItem = getTbItem(addGoods);
        tbItem.setId(SnowIdUtils.uniqueLong());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TbUser tbUser = tbUserService.get(username);
        if (tbUser == null) {
            throw new BusinessException(ExceptionStatus.ACCOUNT_NOT_EXIST);
        }
        int i = tbItemService.addGoods(tbUser.getId(), tbItem, addGoods.getDesc());
        if (i > 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK);
        } else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping()
    public ResponseResult updateGoods(@Valid @RequestBody UploadGoodsDTO uploadGoodsDTO) {
        TbItem tbItem = getTbItem(uploadGoodsDTO);
        tbItem.setId(Long.valueOf(uploadGoodsDTO.getGoodsId()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        if (tbUser == null) {
            throw new BusinessException(ExceptionStatus.ACCOUNT_NOT_EXIST);
        }
        int i = tbItemService.updateMyGoods(tbUser.getId(), tbItem);
        if (i == 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL);
        }
        return new ResponseResult<>(ResponseResult.CodeStatus.OK);
    }

    @GetMapping("/other/{id}")
    public ResponseResult getOtherGoods(@PathVariable(value = "id", required = true) String id) {
        TbUser byId = tbUserService.getById(Long.valueOf(id));
        Date date = new Date();
        int days = (int) ((date.getTime() - byId.getCreateTime().getTime()) / (1000 * 3600 * 24));
        Map<String, Object> map = new HashMap<>(6, 1);
        map.putAll(tbItemService.getOtherGoodsNumberAndSellCount(Long.valueOf(id)));
        map.put("nickName", byId.getNickName());
        map.put("icon", byId.getIcon());
        map.put("comeTime", days);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, map);
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
