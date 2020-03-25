package com.study.shop.provider.api;

import com.study.shop.provider.domain.TbComment;
import com.study.shop.provider.vo.CommentVO;

import java.util.List;

/**
 * @author admin
 */
public interface TbCommentService {

    /**
     * 根据商品编号获取商品所有评论
     *
     * @param goodsId 商品编号
     * @return 评论集合
     */
    List<CommentVO> getCommentByGoods(Long goodsId);

    /**
     * 添加商品评论
     * @param tbComment {@link TbComment}
     * @return 添加商品评论
     */
    int addComment(TbComment tbComment);

    /**
     * 删除评论
     * @param userId Long
     * @param commentId Long
     * @return 0失败 1成功
     */
    int deleteComment(Long userId,Long commentId);
}
