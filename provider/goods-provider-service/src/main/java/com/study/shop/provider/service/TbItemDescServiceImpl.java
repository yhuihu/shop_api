package com.study.shop.provider.service;

import com.study.shop.provider.api.TbItemDescService;
import com.study.shop.provider.domain.TbItemDesc;
import com.study.shop.provider.mapper.TbItemDescMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

/**
 * @author admin
 */
@Service(version = "1.0.0", retries = 0, timeout = 10000)
public class TbItemDescServiceImpl implements TbItemDescService {

    @Resource
    private TbItemDescMapper tbItemDescMapper;

    @Override
    public TbItemDesc getItemDesc(Long itemId) {
        return tbItemDescMapper.selectByPrimaryKey(itemId);
    }


    @Override
    public int updateItemDesc(TbItemDesc tbItemDesc) {
        return tbItemDescMapper.updateByPrimaryKey(tbItemDesc);
    }
}
