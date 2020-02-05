package com.study.shop.provider.api;

import com.github.pagehelper.PageInfo;
import com.study.shop.provider.domain.TbItem;
import com.study.shop.provider.dto.GoodsSearchDTO;
import com.study.shop.provider.vo.GoodDetailVO;
import com.study.shop.provider.vo.GoodsVO;

import java.util.List;

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
     * 功能描述: <br>
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
     * 用户发布闲置物品
     * @param username 用户名
     * @param tbItem 实体
     * @param desc 商品描述
     * @return int 0,1
     */
    int addGoods(String username, TbItem tbItem,String desc);
}
