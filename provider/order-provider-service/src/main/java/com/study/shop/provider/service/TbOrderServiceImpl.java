package com.study.shop.provider.service;

import com.study.shop.provider.api.TbOrderService;
import com.study.shop.provider.domain.TbOrder;
import com.study.shop.provider.mapper.TbOrderMapper;
import com.study.shop.provider.vo.CheckOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 */
@Slf4j
@Service(version = "1.0.0", parameters = {"addOrder.retries", "0", "addOrder.timeout", "30000"})
public class TbOrderServiceImpl implements TbOrderService {

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Override
    public int addOrder(TbOrder tbOrder) {
        try {
            return tbOrderMapper.insert(tbOrder);
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public int checkOrder(Long userId, Long groupId) {
        try {
            Example example = new Example(TbOrder.class);
            example.createCriteria().andEqualTo("groupId", groupId).andEqualTo("userId", userId);
            example.setForUpdate(true);
            List<TbOrder> tbOrders = tbOrderMapper.selectByExample(example);
            tbOrders.forEach(item -> {
                item.setStatus(2);
                tbOrderMapper.updateByPrimaryKeySelective(item);
            });
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public List<CheckOrderVO> getByGroupCheck(Long groupId, Long userId) {
        return tbOrderMapper.getCheckByGroupId(groupId, userId);
    }

    @Override
    public List<TbOrder> getByGroup(Long groupId) {
        Example example = new Example(TbOrder.class);
        example.createCriteria().andEqualTo("groupId", groupId);
        return tbOrderMapper.selectByExample(example);
    }

    @Override
    public int deleteByGroup(Long groupId) {
        Example example = new Example(TbOrder.class);
        example.createCriteria().andEqualTo("groupId", groupId).andEqualTo("status", 0);
        return tbOrderMapper.deleteByExample(example);
    }

    @Override
    public List<TbOrder> getTimeOutOrder(Date date) {
        Example example = new Example(TbOrder.class);
        example.createCriteria().andLessThan("createTime", date);
        return tbOrderMapper.selectByExample(example);
    }

    @Override
    public int changeOrderStatus(Long groupId, Integer status) {
        TbOrder tbOrder=new TbOrder();
        tbOrder.setStatus(status);
        Example example=new Example(TbOrder.class);
        example.createCriteria().andEqualTo("groupId",groupId);
        tbOrderMapper.updateByExampleSelective(tbOrder,example);
        return 0;
    }
}
