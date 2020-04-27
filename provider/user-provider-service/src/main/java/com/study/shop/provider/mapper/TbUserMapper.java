package com.study.shop.provider.mapper;

import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.dto.AdminSearchDTO;
import com.study.shop.provider.vo.AdminUserVO;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**
 * @author admin
 */
public interface TbUserMapper extends MyMapper<TbUser> {
    /**
     * 管理员查看用户列表
     * @param adminSearchDTO {@link AdminSearchDTO}
     * @return {@link AdminUserVO}
     */
    List<AdminUserVO> adminGetUser(AdminSearchDTO adminSearchDTO);
}
