package com.study.shop.provider.mapper;

import com.study.shop.provider.domain.TbItem;
import com.study.shop.provider.dto.GoodsSearchDTO;
import com.study.shop.provider.vo.GoodsVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author admin
 */
public interface TbItemMapper extends Mapper<TbItem> {
    /**
     * 功能描述: 查询推荐
     *
     * @param input 输入参数
     * @return List<String>
     */
    List<String> searchRecommend(@Param("input") String input);

    /**
     * 功能描述: 动态查询商品
     *
     * @param goodsSearchDTO {@link GoodsSearchDTO}
     * @return List<GoodsVO> {@link GoodsVO}
     */
    List<GoodsVO> getGoodList(GoodsSearchDTO goodsSearchDTO);
}
