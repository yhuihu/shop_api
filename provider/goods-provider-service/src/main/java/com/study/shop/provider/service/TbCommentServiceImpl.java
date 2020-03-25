package com.study.shop.provider.service;

import com.study.shop.provider.api.TbCommentService;
import com.study.shop.provider.domain.TbComment;
import com.study.shop.provider.mapper.TbCommentMapper;
import com.study.shop.provider.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 */
@Slf4j
@Service(version = "1.0.0", parameters = {"addComment.retries", "0", "addComment.timeout", "30000", "deleteComment.retries", "0", "deleteComment.timeout", "30000"})
public class TbCommentServiceImpl implements TbCommentService {

    @Resource
    private TbCommentMapper tbCommentMapper;

    @Override
    public List<CommentVO> getCommentByGoods(Long goodsId) {
        return tbCommentMapper.getAllComment(goodsId);
    }

    @Override
    public int addComment(TbComment tbComment) {
        try {
            return tbCommentMapper.insert(tbComment);
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public int deleteComment(Long userId, Long commentId) {
        try {
            Example example = new Example(TbComment.class);
            example.createCriteria().andEqualTo("userId", userId).andEqualTo("id", commentId);
            example.or().orEqualTo("replyId",commentId);
            return tbCommentMapper.deleteByExample(example);
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }
}
