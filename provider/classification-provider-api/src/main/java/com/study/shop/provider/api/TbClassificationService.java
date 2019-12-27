package com.study.shop.provider.api;

import com.study.shop.provider.domain.TbClassification;
import com.study.shop.provider.vo.ClassificationVO;

import java.util.List;

/**
 * @author Tiger
 * @date 2019-11-19
 * @see com.study.shop.provider.api
 **/
public interface TbClassificationService {
    /**
     * 通过id获取商品分类
     *
     * @param id 编号
     * @return {@link TbClassification}
     */
    TbClassification getClassificationById(Long id);

    /**
     * 通过父分类id获取子分类集合
     *
     * @param id 编号
     * @return {@link TbClassification}
     */
    List<TbClassification> getClassificationList(Long id);

    /**
     * 获取所有分类集
     * @return {@link ClassificationVO}
     */
    List<ClassificationVO> getAllClassification();

    /**
     * 通过父分类id获取子分类集合
     *
     * @param tbClassification 分类实体
     * @return int
     */
    int addClassification(TbClassification tbClassification);

    /**
     * 编辑商品分类
     *
     * @param tbClassification 分类实体
     * @return int
     */
    int updateClassification(TbClassification tbClassification);

    /**
     * 删除商品分类
     *
     * @param id 编号
     */
    void deleteClassification(Long id);


}
