package com.study.shop.provider.api;

import com.study.shop.provider.domain.TbItemDesc;

/**
 * @author admin
 */
public interface TbItemDescService{

    /**
     * 功能描述: 获取详细信息
     * @param itemId 商品编号
     * @return {@link TbItemDesc}
     */
    TbItemDesc getItemDesc(Long itemId);

    /**
     * 功能描述: 更新商品描述
     * @param tbItemDesc 需要更新的内容实体
     * @return int
     */
    int updateItemDesc(TbItemDesc tbItemDesc);
}
