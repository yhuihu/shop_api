package com.study.shop.provider.mapper;

import com.study.shop.provider.domain.TbOrder;
import com.study.shop.provider.vo.CheckOrderVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author admin
 */
public interface TbOrderMapper extends Mapper<TbOrder> {
    /**
     * 获取确认订单列表内容
     * @param groupId groupId
     * @param userId userId
     * @return {@link CheckOrderVO}
     */
    List<CheckOrderVO> getCheckByGroupId(@Param("groupId") Long groupId, @Param("userId")Long userId);
}
