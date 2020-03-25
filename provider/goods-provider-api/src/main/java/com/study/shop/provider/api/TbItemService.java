package com.study.shop.provider.api;

import com.github.pagehelper.PageInfo;
import com.study.shop.provider.domain.TbItem;
import com.study.shop.provider.dto.GoodsSearchDTO;
import com.study.shop.provider.dto.MyGoodsDTO;
import com.study.shop.provider.vo.GoodDetailVO;
import com.study.shop.provider.vo.GoodsVO;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
public interface TbItemService {
    /**
     * 功能描述: 搜索推荐
     *
     * @param input 输入内容
     * @return List<String>
     */
    List<String> searchRecommend(String input);

    /**
     * 功能描述: 获取搜索商品结果
     *
     * @param goodsSearchDTO {@link GoodsSearchDTO 搜索实体类}
     * @return List<TbItem>
     */
    PageInfo<GoodsVO> searchItem(GoodsSearchDTO goodsSearchDTO);

    /**
     * 功能描述: 获取商品详情<br>
     * @param goodId  Long
     * @return GoodDetailVO {@link GoodDetailVO}
     */
    GoodDetailVO getGoodDetail(Long goodId);

    /**
     * 获取购物车所有商品信息
     * @param productIdList 商品编号
     * @return List<GoodsVO>
     */
    List<GoodsVO> getCartDetail(List<Long> productIdList);

    /**
     * 获取日志商品详情
     * @param goodsIds id集合
     * @return GoodDetailVO
     */
    List<GoodDetailVO> getLogsGoodsDetail(List<Long> goodsIds);
    /**
     * 用户获取自己发布的闲置物品
     * @param myGoodsDTO 实体
     * @param userId 用户编号
     * @return PageInfo<GoodsVO> {@link GoodsVO}
     */
    PageInfo<GoodsVO> getMyGoods(MyGoodsDTO myGoodsDTO,Long userId);

    /**
     * 用户更新时加载数据
     * @param userId 用户编号
     * @param goodsId 商品编号
     * @return GoodDetailVO {@link GoodDetailVO}
     */
    GoodDetailVO getMyGoodsDetail(Long userId,Long goodsId);

    /**
     * 用户发布闲置物品
     * @param userId 用户名
     * @param tbItem 实体
     * @param desc 商品描述
     * @return int 0,1
     */
    int addGoods(Long userId, TbItem tbItem,String desc);

    /**
     * 用户删除已发布的闲置物品
     * @param userId 用户编号
     * @param goodsId 商品编号
     * @return 0,1
     */
    int deleteGoods(Long userId,Long goodsId);

    /**
     * 用户更新自己的闲置物品信息
     * @param userId 用户编号
     * @param tbItem 实体
     * @return 0,1
     */
    int updateMyGoods(Long userId,TbItem tbItem);

    /**
     * 更改商品状态
     * @param goodsId 商品编号
     * @param status 状态
     * @return 0,1
     */
    int changeGoodsStatus(List<Long> goodsId,Integer status);

    /**
     * 获取用户商品数量与售出数量
     * @param userId userId
     * @return Map集合
     */
    Map<String,Object> getOtherGoodsNumberAndSellCount(Long userId);
}
