package com.study.shop.provider.service;

import com.study.shop.provider.api.ShippingService;
import com.study.shop.provider.domain.TbOrder;
import com.study.shop.provider.dto.ShippingDTO;
import com.study.shop.provider.mapper.TbOrderMapper;
import org.apache.dubbo.config.annotation.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Tiger
 * @date 2020-03-27
 * @see com.study.shop.provider.service
 **/
@Service(version = "1.0.0", parameters = {"postSkipping.retries", "0", "postSkipping.timeout", "60000"})
public class ShippingServiceImpl implements ShippingService {
    @Resource
    private TbOrderMapper tbOrderMapper;

    @Override
    public int postSkipping(ShippingDTO shippingDTO, Long userId) {
        Example example = new Example(TbOrder.class);
        example.createCriteria().andEqualTo("goodsId", shippingDTO.getGoodsId()).
                andEqualTo("status", "2");
        TbOrder tbOrder = new TbOrder();
        tbOrder.setStatus(3);
        tbOrder.setShippingName(shippingDTO.getShippingName());
        tbOrder.setShippingCode(shippingDTO.getShippingCode());
        tbOrder.setConsignTime(new Date());
        return tbOrderMapper.updateByExampleSelective(tbOrder, example);
    }
}
