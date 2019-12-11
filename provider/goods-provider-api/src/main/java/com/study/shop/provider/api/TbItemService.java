package com.study.shop.provider.api;

import com.study.shop.provider.domain.TbItem;

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
     * @param page    页数
     * @param size    单页条数
     * @param sort    排序规则 1价格升序 -1价格降序 0默认
     * @param priceGt 价格区间最低价格
     * @param priceLt 价格区间最高价格
     * @param keyword 关键字
     * @return List<TbItem>
     */
    List<TbItem> searchItem(Integer page, Integer size, Integer sort, Double priceGt, Double priceLt, String keyword);
}
