package com.study.shop.provider.service;

import com.study.shop.provider.api.CartService;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.domain.Cart;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Tiger
 * @date 2020-01-21
 * @see com.study.shop.provider.service
 **/
@Service(version = "1.0.0", retries = 0, timeout = 10000)
public class CartServiceImpl implements CartService {
    @Reference(version = "1.0.0")
    private TbItemService tbItemService;
    @Resource
    RedisTemplate<String, Object> redisTemplate;
    private final String CART_KEY = "cartList_";
  // zhegge ?对 你的controller呢
    @Override
    public int addCart(List<Cart> list, String username) {
        try {
            HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
            String key = CART_KEY + username;
            for (Cart cartData : list) {
                Cart cartData1 = (Cart) hashOperations.get(key, cartData.getProductId());
                if (cartData1 != null) {
                    Integer productNum = cartData1.getProductNum() + cartData.getProductNum();
                    cartData.setProductNum(productNum);
                }
                hashOperations.put(key, cartData.getProductId(), cartData);
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Cart> getAllCart(String username) {
        String key = CART_KEY + username;
        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();
        return hashOperations.values(key);
    }
}
