package com.study.shop.provider.api;

import com.github.pagehelper.PageInfo;
import com.study.shop.provider.dto.GoodsSearchDTO;
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
}
