package com.study.shop.business.controller;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbFollowService;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbFollow;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.utils.SnowIdUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Tiger
 * @date 2020-03-21
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("follow")
public class FollowController {
    @Reference(version = "1.0.0")
    private TbUserService tbUserService;

    @Reference(version = "1.0.0")
    private TbFollowService tbFollowService;

    @GetMapping()
    @PreAuthorize("hasAuthority('USER')")
    public ResponseResult getMyFollow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        return new ResponseResult(ResponseResult.CodeStatus.OK, tbFollowService.getMyFollow(tbUser.getId()));
    }

    /**
     * 添加关注
     *
     * @param targetId targetId
     * @return ResponseResult
     */
    @PostMapping("/{targetId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseResult addFollow(@PathVariable(value = "targetId") String targetId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        TbUser tbUser = tbUserService.get(username);
        if (tbUser.getId().equals(Long.valueOf(targetId))) {
            return new ResponseResult(ResponseResult.CodeStatus.FAIL, "不能关注自己哦");
        }
        TbFollow tbFollow = new TbFollow();
        tbFollow.setId(SnowIdUtils.uniqueLong());
        tbFollow.setUserId(tbUser.getId());
        tbFollow.setTargetId(Long.valueOf(targetId));
        tbFollow.setCreateTime(new Date());
        int i = tbFollowService.addFollow(tbFollow);
        if (i > 0) {
            return new ResponseResult(ResponseResult.CodeStatus.OK);
        } else {
            return new ResponseResult(ResponseResult.CodeStatus.FAIL, "服务器操作异常。");
        }
    }

    /**
     * 取消关注
     *
     * @param targetId targetId
     * @return ResponseResult
     */
    @DeleteMapping("/{targetId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseResult unFollow(@PathVariable(value = "targetId") String targetId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (tbFollowService.unFollow(tbUserService.get(username).getId(), Long.valueOf(targetId)) == 0) {
            return new ResponseResult(ResponseResult.CodeStatus.FAIL, "取消关注失败，请稍后重试");
        }
        return new ResponseResult(ResponseResult.CodeStatus.OK);
    }

    /**
     * 判断是否关注
     *
     * @param targetId targetId
     * @return ResponseResult
     */
    @GetMapping("/{targetId}")
    public ResponseResult checkFollow(@PathVariable(value = "targetId") String targetId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if ("anonymousUser".equals(username)) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "关注");
        }
        TbUser tbUser = tbUserService.get(username);
        if (tbUser == null) {
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "关注");
        } else {
            int i = tbFollowService.checkFollow(tbUser.getId(), Long.valueOf(targetId));
            if (i > 0) {
                return new ResponseResult<>(ResponseResult.CodeStatus.OK, "取消关注");
            } else {
                return new ResponseResult<>(ResponseResult.CodeStatus.OK, "关注");
            }
        }
    }
}
