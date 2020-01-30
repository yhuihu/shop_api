package com.study.shop.provider.api;

import com.study.shop.provider.dto.AddressDTO;
import com.study.shop.provider.vo.TbAddressVO;

import java.util.List;

/**
 * @author Tiger
 */
public interface TbAddressService{

    /**
     * 获取用户的收货地址
     * @param username 用户名
     * @return List<TbAddressVO> {@link TbAddressVO}
     */
    List<TbAddressVO> getAllAddress(String username);

    /**
     * 添加地址
     * @param username 用户名
     * @param addressDTO 地址实体
     * @return int 0,1
     */
    int addAddress(String username, AddressDTO addressDTO);

    /**
     * 更新地址
     * @param username 用户名
     * @param addressDTO 地址实体
     * @return 0,1
     */
    int updateAddress(String username, AddressDTO addressDTO);

    /**
     * 删除地址
     * @param username 用户名
     * @param addressId 地址编号
     * @return 0,1
     */
    int deleteAddress(String username,Long addressId);
}
