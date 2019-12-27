package com.study.shop.provider.mapper;


import com.study.shop.provider.domain.TbClassification;
import com.study.shop.provider.vo.ClassificationVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**
 * @author admin
 */
public interface TbClassificationMapper extends MyMapper<TbClassification> {
    /**
     * 功能描述: 查询所有分类形成的树状结构<br>
     * @param parentId 递归时用
     * @return List<ClassificationVO> l
     */
    List<ClassificationVO> getClassificationList(@Param("parentId") Long parentId);
}
