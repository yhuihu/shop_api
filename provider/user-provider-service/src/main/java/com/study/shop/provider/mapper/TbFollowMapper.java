package com.study.shop.provider.mapper;

import com.study.shop.provider.domain.TbFollow;
import com.study.shop.provider.dto.FollowUserDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbFollowMapper extends Mapper<TbFollow> {
    /**
     * 获取我关注的人
     * @param userId userId
     * @return {@link FollowUserDTO}
     */
    List<FollowUserDTO> getMyFollow(Long userId);
}
