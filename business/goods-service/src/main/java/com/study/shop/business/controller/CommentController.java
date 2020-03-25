package com.study.shop.business.controller;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbCommentService;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbComment;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.dto.CommentDTO;
import com.study.shop.provider.vo.CommentVO;
import com.study.shop.utils.SnowIdUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author Tiger
 * @date 2020-01-31
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("comment")
public class CommentController {
    @Reference(version = "1.0.0")
    private TbCommentService tbCommentService;

    @Reference(version = "1.0.0")
    private TbUserService tbUserService;

    @GetMapping("/{goodId}")
    public ResponseResult getGoodComment(@PathVariable(name = "goodId") String goodId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        if (tbUser == null) {
            tbUser = new TbUser();
        }
        List<CommentVO> commentByGoods = tbCommentService.getCommentByGoods(Long.valueOf(goodId));
        for (CommentVO commentVO : commentByGoods) {
            if (Long.valueOf(commentVO.getUserId()).equals(tbUser.getId())) {
                commentVO.setIsMine(1);
            } else {
                commentVO.setIsMine(0);
            }
            if (commentVO.getChildren().size() > 0) {
                for (CommentVO comment : commentVO.getChildren()) {
                    if (Long.valueOf(comment.getUserId()).equals(tbUser.getId())) {
                        comment.setIsMine(1);
                    } else {
                        comment.setIsMine(0);
                    }
                }
            }
        }
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取商品评论", commentByGoods);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('USER')")
    public ResponseResult addComment(@RequestBody CommentDTO commentDTO) {
//        TbUser tbUser = tbUserService.get(commentDTO.getReplyUser());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        TbComment tbComment = new TbComment();
        tbComment.setId(SnowIdUtils.uniqueLong());
        tbComment.setContent(commentDTO.getContent());
        tbComment.setCreateTime(new Date());
        tbComment.setGoodsId(Long.valueOf(commentDTO.getGoodsId()));
        tbComment.setReplyId(Long.valueOf(commentDTO.getReplyId()));
        tbComment.setUserId(tbUser.getId());
        int i = tbCommentService.addComment(tbComment);
        if (i > 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "添加评论成功");
        } else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "添加评论失败");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseResult deleteComment(@PathVariable(name = "id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        int i = tbCommentService.deleteComment(tbUser.getId(),Long.valueOf(id));
        if (i > 0) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "删除评论成功");
        } else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "删除评论失败");
        }
    }

}
