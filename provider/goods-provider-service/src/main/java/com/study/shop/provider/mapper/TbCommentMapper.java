package com.study.shop.provider.mapper;

import com.study.shop.provider.domain.TbComment;
import com.study.shop.provider.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author admin
 */
public interface TbCommentMapper extends Mapper<TbComment> {
    /**
     * 嵌套查询所有评论
     *
     * @param id 商品编号
     * @return List<CommentVO> {@link CommentVO}
     */
    List<CommentVO> getAllComment(@Param(value = "id") Long id);
}
