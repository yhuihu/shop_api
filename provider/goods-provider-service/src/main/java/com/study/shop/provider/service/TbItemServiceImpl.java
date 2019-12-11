package com.study.shop.provider.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.domain.TbItem;
import com.study.shop.provider.mapper.TbItemMapper;
import org.apache.dubbo.config.annotation.Service;
import tk.mybatis.mapper.entity.Example;

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

    @Override
    public List<TbItem> searchItem(Integer page, Integer size, Integer sort, Double priceGt, Double priceLt, String keyword) {
        Example example = new Example(TbItem.class);
//        asc升，desc降
        int value = sort;
        if (value != 0) {
            if (value == 1) {
                example.orderBy("price").asc();
            } else if (value == -1) {
                example.orderBy("price").desc();
            }
        }
        example.createCriteria().andLike("title", "%" + keyword + "%").orLike("sellPoint", "%" + keyword + "%");
        PageHelper.startPage(page, size);
        List<TbItem> list = tbItemMapper.selectByExample(example);
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        List<TbItem> list1 = pageInfo.getList();
        return list1;
    }
}
