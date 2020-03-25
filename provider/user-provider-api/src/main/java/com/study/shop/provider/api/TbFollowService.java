package com.study.shop.provider.api;

import com.study.shop.provider.domain.TbFollow;
import com.study.shop.provider.dto.FollowUserDTO;

import java.util.List;

public interface TbFollowService{

    /**
     * 添加关注人
     * @param tbFollow {@link TbFollow}
     * @return 0,1
     */
    int addFollow(TbFollow tbFollow);

    /**
     * 判断是否已关注
     * @param userId userId
     * @param targetId targetId
     * @return 0,1
     */
    int checkFollow(Long userId,Long targetId);

    /**
     * 取消关注
     * @param userId userId
     * @param targetId targetId
     * @return 0,1
     */
    int unFollow(Long userId,Long targetId);

    /**
     * 获取我关注的人
     * @param userId userId
     * @return {@link FollowUserDTO}
     */
    List<FollowUserDTO> getMyFollow(Long userId);
}
