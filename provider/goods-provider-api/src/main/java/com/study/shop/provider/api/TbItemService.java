package com.study.shop.provider.api;

import java.util.List;

/**
 * @author admin
 */
public interface TbItemService{
    /**
     * 功能描述: 搜索推荐
     * @param input 输入内容
     * @return List<String>
     */
    List<String> searchRecommend(String input);
}
