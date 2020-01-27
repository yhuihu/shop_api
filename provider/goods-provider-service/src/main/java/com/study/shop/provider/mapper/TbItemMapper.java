package com.study.shop.provider.mapper;

import com.study.shop.provider.domain.TbItem;
import com.study.shop.provider.dto.GoodsSearchDTO;
import com.study.shop.provider.vo.GoodDetailVO;
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

    /**
     * 功能描述: 查看商品详细信息<br>
     *
     * @param goodId 商品编号
     * @return GoodDetailVO {@link GoodDetailVO}
     */
    GoodDetailVO getGoodDetail(Long goodId);

    /**
     * 获取详情数据集
     * @param productIds id数据集
     * @return List<GoodsVO>
     */
    List<GoodsVO> getCartDetail(List<Long> productIds);
}
