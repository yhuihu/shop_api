package com.study.shop.provider.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.shop.provider.api.TbItemDescService;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.domain.TbItem;
import com.study.shop.provider.domain.TbItemDesc;
import com.study.shop.provider.dto.AdminSearchGoodsDTO;
import com.study.shop.provider.dto.GoodsSearchDTO;
import com.study.shop.provider.dto.MyGoodsDTO;
import com.study.shop.provider.mapper.TbItemMapper;
import com.study.shop.provider.vo.GoodDetailVO;
import com.study.shop.provider.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
@Slf4j
@Service(version = "1.0.0", retries = 0, timeout = 10000)
public class TbItemServiceImpl implements TbItemService {

    @Resource
    private TbItemMapper tbItemMapper;

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
    public List<GoodDetailVO> getLogsGoodsDetail(List<Long> goodsIds) {
        List<GoodDetailVO> goodDetailVOS = new ArrayList<>();
        goodsIds.forEach(item -> {
            goodDetailVOS.add(tbItemMapper.getLogGoodDetail(item));
        });
        return goodDetailVOS;
    }

    @Override
    public PageInfo<GoodsVO> getMyGoods(MyGoodsDTO myGoodsDTO, Long userId) {
        myGoodsDTO.setUserId(userId);
        if (myGoodsDTO.getPage() == null) {
            myGoodsDTO.setPage(1);
        }
        if (myGoodsDTO.getSize() == null) {
            myGoodsDTO.setSize(10);
        }
        PageHelper.startPage(myGoodsDTO.getPage(), myGoodsDTO.getSize());
        List<GoodsVO> myGoods = tbItemMapper.getMyGoods(myGoodsDTO);
        return new PageInfo<>(myGoods);
    }

    @Override
    public GoodDetailVO getMyGoodsDetail(Long userId, Long goodsId) {
        GoodDetailVO goodDetail = tbItemMapper.getGoodDetail(goodsId);
        if (!goodDetail.getUserId().equals(userId)) {
            return null;
        } else {
            return goodDetail;
        }
    }

    @Override
    public int addGoods(Long userId, TbItem tbItem, String desc) {
        tbItem.setUserId(userId);
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

    @Override
    public int deleteGoods(Long userId, Long goodsId) {
        Example example = new Example(TbItem.class);
        example.createCriteria().andEqualTo("id", goodsId);
        TbItem tbItem = tbItemMapper.selectOneByExample(example);
        if (!tbItem.getUserId().equals(userId)) {
            return 0;
        } else {
            try {
                tbItem.setStatus(4);
                return tbItemMapper.updateByPrimaryKey(tbItem);
            } catch (Exception e) {
                log.error(e.getMessage());
                return 0;
            }
        }
    }

    @Override
    public int updateMyGoods(Long userId, TbItem tbItem) {
        Example example = new Example(TbItem.class);
        example.createCriteria().andEqualTo("id", tbItem.getId());
        TbItem tbItem1 = tbItemMapper.selectOneByExample(example);
        if (!userId.equals(tbItem1.getUserId())) {
            return 0;
        } else if (2 == tbItem1.getStatus()) {
            return 0;
        } else {
            Date oldDate = tbItem1.getCreated();
            BeanUtils.copyProperties(tbItem, tbItem1, "null");
            try {
                tbItem1.setUserId(userId);
                tbItem1.setCreated(oldDate);
                tbItem1.setUpdated(new Date());
                return tbItemMapper.updateByPrimaryKey(tbItem1);
            } catch (Exception e) {
                log.error(e.getMessage());
                return 0;
            }
        }
    }

    @Override
    public int changeGoodsStatus(List<Long> goodsId, Integer status) {
        try {
            for (Long aLong : goodsId) {
                TbItem tbItem = tbItemMapper.selectByPrimaryKey(aLong);
                tbItem.setStatus(status);
                int i = tbItemMapper.updateByPrimaryKey(tbItem);
                if (i == 0) {
                    return 0;
                }
            }
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public Map<String, Object> getOtherGoodsNumberAndSellCount(Long userId) {
        Example example = new Example(TbItem.class);
        example.createCriteria().andEqualTo("userId", userId).andNotEqualTo("status", 4).
                andNotEqualTo("status", 3).andNotEqualTo("status", 0);
        List<TbItem> list = tbItemMapper.selectByExample(example);
        int goodsCount = list.size();
        int sellCount = 0;
        for (TbItem tbItem : list) {
            // 订单完成后状态将修改为2
            if (tbItem.getStatus() == 2) {
                sellCount++;
            }
        }
        Map<String, Object> map = new HashMap<>(3, 1);
        map.put("goodsList", list);
        map.put("goodsCount", goodsCount);
        map.put("sellCount", sellCount);
        return map;
    }

    @Override
    public PageInfo<GoodsVO> adminGetGoodsListByPage(AdminSearchGoodsDTO adminSearchGoodsDTO) {
        if (adminSearchGoodsDTO.getPage() == null || adminSearchGoodsDTO.getPage() <= 0) {
            adminSearchGoodsDTO.setPage(1);
        }
        if (adminSearchGoodsDTO.getSize() == null || adminSearchGoodsDTO.getSize() <= 0) {
            adminSearchGoodsDTO.setSize(5);
        }
        PageHelper.startPage(adminSearchGoodsDTO.getPage(), adminSearchGoodsDTO.getSize());
        List<GoodsVO> goodsVOS = tbItemMapper.adminGetGoodsList(adminSearchGoodsDTO);
        return new PageInfo<>(goodsVOS);
    }

    @Override
    public int adminDeleteGoods(Long id) {
        Example example = new Example(TbItem.class);
        example.createCriteria().andEqualTo("id", id).andEqualTo("status", "1");
        return tbItemMapper.deleteByExample(example);
    }
}
