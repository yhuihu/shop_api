package com.study.shop.provider.service;

import com.study.shop.provider.api.TbFollowService;
import com.study.shop.provider.domain.TbFollow;
import com.study.shop.provider.dto.FollowUserDTO;
import com.study.shop.provider.mapper.TbFollowMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 */
@Slf4j
@Service(version = "1.0.0", parameters = {"addFollow.retries", "0", "addFollow.timeout", "30000"})
public class TbFollowServiceImpl implements TbFollowService {

    @Resource
    private TbFollowMapper tbFollowMapper;

    @Override
    public int addFollow(TbFollow tbFollow) {
        try {
            tbFollowMapper.insert(tbFollow);
            return 1;
        } catch (Exception e) {
            log.error("添加关注人异常：{}", e.getMessage());
            return 0;
        }
    }

    @Override
    public int checkFollow(Long userId, Long targetId) {
        Example example = new Example(TbFollow.class);
        example.createCriteria().andEqualTo("userId", userId).andEqualTo("targetId", targetId);
        return tbFollowMapper.selectOneByExample(example) == null ? 0 : 1;
    }

    @Override
    public int unFollow(Long userId, Long targetId) {
        Example example=new Example(TbFollow.class);
        example.createCriteria().andEqualTo("userId", userId).andEqualTo("targetId", targetId);
        return tbFollowMapper.deleteByExample(example);
    }

    @Override
    public List<FollowUserDTO> getMyFollow(Long userId) {
        return tbFollowMapper.getMyFollow(userId);
    }
}
