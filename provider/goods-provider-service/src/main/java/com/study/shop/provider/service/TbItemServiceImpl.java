package com.study.shop.provider.service;

import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.mapper.TbItemMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 */
@Service(version = "1.0.0", retries = 0, timeout = 10000)
public class TbItemServiceImpl implements TbItemService {

    @Resource
    private TbItemMapper tbItemMapper;

    @Override
    public List<String> searchRecommend(String input) {
        return tbItemMapper.searchRecommend(input);
    }
}
