package com.study.shop.provider.service;

import com.study.shop.provider.api.TbAddressService;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbAddress;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.dto.AddressDTO;
import com.study.shop.provider.mapper.TbAddressMapper;
import com.study.shop.provider.vo.TbAddressVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Admin
 */
@Service(version = "1.0.0")
@Slf4j
public class TbAddressServiceImpl implements TbAddressService {
    @Resource
    TbUserService tbUserService;

    @Resource
    private TbAddressMapper tbAddressMapper;

    @Override
    public List<TbAddressVO> getAllAddress(String username) {
        TbUser tbUser = tbUserService.get(username);
        return tbAddressMapper.selectAllAddress(tbUser.getId());
    }

    @Override
    public int addAddress(String username, AddressDTO addressDTO) {
        TbUser tbUser = tbUserService.get(username);
        TbAddress tbAddress = new TbAddress();
        BeanUtils.copyProperties(addressDTO, tbAddress);
        tbAddress.setUserId(tbUser.getId());
        if (checkDuplicate(addressDTO, tbUser)) {
            return 0;
        }
        return tbAddressMapper.insert(tbAddress);
    }

    @Override
    public int updateAddress(String username, AddressDTO addressDTO) {
        TbUser tbUser = tbUserService.get(username);
        Example example = new Example(TbAddress.class);
        example.createCriteria().andEqualTo("userId", tbUser.getId()).andEqualTo("addressId", addressDTO.getAddressId());
        TbAddress tbAddress = tbAddressMapper.selectOneByExample(example);
        if (tbAddress != null) {
            if (checkDuplicate(addressDTO, tbUser)) {
                return 0;
            }
            BeanUtils.copyProperties(addressDTO, tbAddress);
            try {
                return tbAddressMapper.updateByPrimaryKey(tbAddress);
            } catch (Exception e) {
                log.error("更新地址出现错误:{}\n", e.getMessage());
                return 0;
            }
        }
        return 0;
    }

    @Override
    public int deleteAddress(String username, Long addressId) {
        TbUser tbUser = tbUserService.get(username);
        Example example = new Example(TbAddress.class);
        example.createCriteria().andEqualTo("addressId", addressId)
                .andEqualTo("userId", tbUser.getId());
        try {
            TbAddress tbAddress = tbAddressMapper.selectOneByExample(example);
            if (tbAddress.getIsDefault()) {
                Example example1 = new Example(TbAddress.class);
                example1.createCriteria().andEqualTo("userId", tbUser.getId())
                        .andEqualTo("isDefault", false);
                TbAddress tbAddress1 = tbAddressMapper.selectByExample(example1).get(0);
                tbAddress1.setIsDefault(true);
                tbAddressMapper.updateByPrimaryKey(tbAddress1);
            }
            return tbAddressMapper.deleteByExample(example);
        } catch (Exception e) {
            log.error("删除地址出现错误:{}\n", e.getMessage());
            return 0;
        }
    }

    private boolean checkDuplicate(AddressDTO addressDTO, TbUser tbUser) {
        if (addressDTO.getIsDefault()) {
            Example example1 = new Example(TbAddress.class);
            example1.createCriteria().andEqualTo("userId", tbUser.getId()).andEqualTo("isDefault", true);
            example1.setDistinct(true);
            TbAddress tbAddress1 = tbAddressMapper.selectOneByExample(example1);
            if (tbAddress1 != null) {
                tbAddress1.setIsDefault(false);
                try {
                    tbAddressMapper.updateByPrimaryKey(tbAddress1);
                } catch (Exception e) {
                    log.error("更新默认地址出现错误:{}\n", e.getMessage());
                    return true;
                }
            }
        }
        return false;
    }
}
