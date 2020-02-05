package com.study.shop.provider.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.shop.provider.api.TbItemDescService;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbItem;
import com.study.shop.provider.domain.TbItemDesc;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.dto.GoodsSearchDTO;
import com.study.shop.provider.mapper.TbItemMapper;
import com.study.shop.provider.vo.GoodDetailVO;
import com.study.shop.provider.vo.GoodsVO;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 */
@Service(version = "1.0.0", retries = 0, timeout = 10000)
public class TbItemServiceImpl implements TbItemService {

    @Resource
    private TbItemMapper tbItemMapper;

    @Reference(version = "1.0.0")
    private TbUserService tbUserService;

    @Resource
    private TbItemDescService tbItemDescService;

    @Override
    public List<String> searchRecommend(String input) {
        return tbItemMapper.searchRecommend(input);
    }

    @Override
    public PageInfo<GoodsVO> searchItem(GoodsSearchDTO goodsSearchDTO) {
        if (goodsSearchDTO.getPage() == null) {
            goodsSearchDTO.setPage(1);
        }
        if (goodsSearchDTO.getSize() == null) {
            goodsSearchDTO.setSize(10);
        }
        PageHelper.startPage(goodsSearchDTO.getPage(), goodsSearchDTO.getSize());
        List<GoodsVO> list = tbItemMapper.getGoodList(goodsSearchDTO);
        return new PageInfo<>(list);
    }

    @Override
    public GoodDetailVO getGoodDetail(Long goodId) {
        return tbItemMapper.getGoodDetail(goodId);
    }

    @Override
    public List<GoodsVO> getCartDetail(List<Long> productIdList) {
        return tbItemMapper.getCartDetail(productIdList);
    }

    @Override
    public int addGoods(String username, TbItem tbItem, String desc) {
        TbUser tbUser = tbUserService.get(username);
        tbItem.setUserId(tbUser.getId());
        tbItem.setUpdated(new Date());
        tbItem.setCreated(new Date());
        try {
            tbItemMapper.insert(tbItem);
            try {
                TbItemDesc tbItemDesc = new TbItemDesc();
                tbItemDesc.setItemId(tbItem.getId());
                tbItemDesc.setCreated(new Date());
                tbItemDesc.setUpdated(new Date());
                tbItemDesc.setItemDesc(desc);
                tbItemDescService.insertItemDesc(tbItemDesc);
                return 1;
            } catch (Exception e) {
                tbItemMapper.delete(tbItem);
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}
