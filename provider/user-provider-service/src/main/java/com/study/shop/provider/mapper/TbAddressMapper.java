package com.study.shop.provider.mapper;

import com.study.shop.provider.domain.TbAddress;
import com.study.shop.provider.vo.TbAddressVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author admin
 */
public interface TbAddressMapper extends Mapper<TbAddress> {
    /**
     * 查询所有地址，把id转为String类型
     *
     * @param userId id
     * @return List<TbAddressVO> {@link TbAddressVO}
     */
    List<TbAddressVO> selectAllAddress(@Param("id") Long userId);
}
