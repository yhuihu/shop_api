package com.study.shop.provider.api;

import com.study.shop.provider.dto.ShippingDTO;

/**
 * @author Tiger
 * @date 2020-03-27
 * @see com.study.shop.provider.api
 **/
public interface ShippingService {
    /**
     * 发货
     * @param shippingDTO {@link ShippingDTO}
     * @param userId u
     * @return 0,1
     */
    int postSkipping(ShippingDTO shippingDTO, Long userId);
}
