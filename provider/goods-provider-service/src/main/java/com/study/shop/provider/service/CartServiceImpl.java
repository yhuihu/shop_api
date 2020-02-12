package com.study.shop.provider.service;

import com.study.shop.provider.api.CartService;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.domain.Cart;
import com.study.shop.provider.vo.GoodDetailVO;
import com.study.shop.provider.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Tiger
 * @date 2020-01-21
 * @see com.study.shop.provider.service
 **/
@Slf4j
@Service(version = "1.0.0", retries = 0, timeout = 10000)
public class CartServiceImpl implements CartService {
    @Reference(version = "1.0.0")
    private TbItemService tbItemService;
    @Resource
    RedisTemplate<String, Object> redisTemplate;
    private final String CART_KEY = "cartList_";

    @Override
    public int addCart(List<Cart> list, String username) {
        try {
            HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
            String key = CART_KEY + username;

            for (Cart cartData : list) {
                Cart cartData1 = (Cart) hashOperations.get(key, cartData.getProductId());
                if (cartData1 != null) {
                    return 0;
                }
                GoodDetailVO goodDetail = tbItemService.getGoodDetail(Long.valueOf(cartData.getProductId()));
                cartData.setProductImg(goodDetail.getImage().split(",")[0]);
                cartData.setChecked("1");
                hashOperations.put(key, cartData.getProductId(), cartData);
            }
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public List<Cart> getAllCart(String username) throws ExecutionException, InterruptedException {
        String key = CART_KEY + username;
        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();
        List<Cart> values = hashOperations.values(key);
        List<Long> longList = new ArrayList<>(values.size());
        values.forEach(item -> {
            longList.add(Long.valueOf(item.getProductId()));
        });
        if (!longList.isEmpty()) {
            List<GoodsVO> cartDetail = tbItemService.getCartDetail(longList);
            Map<String, GoodsVO> cartMap = cartDetail.stream().collect(Collectors.toMap(GoodsVO::getId, goodsVO -> goodsVO));
            for (Cart value : values) {
                GoodsVO goodsVO = cartMap.get(value.getProductId());
                value.setProductName(goodsVO.getTitle());
                value.setSalePrice(goodsVO.getPrice());
                value.setProductImg(goodsVO.getImage().split(",")[0]);
            }
        }
        return values;
    }

    @Override
    public int deleteCart(String username, String productId) {
        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();
        String key = CART_KEY + username;
        try {
            hashOperations.delete(key, productId);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public int deleteCheck(String username) {
        String key = CART_KEY + username;
        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();
        List<Cart> values = hashOperations.values(key);
        try {
            values.forEach(item -> {
                if ("1".equals(item.getChecked())) {
                    hashOperations.delete(key, item.getProductId());
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
        return 1;
    }

    @Override
    public int allCheck(String username, String check) {
        String key = CART_KEY + username;
        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();
        List<Cart> values = hashOperations.values(key);
        try {
            for (Cart value : values) {
                if ("true".equals(check)) {
                    value.setChecked("1");
                } else if ("false".equals(check)) {
                    value.setChecked("0");
                } else {
                    return 0;
                }
                hashOperations.put(key, value.getProductId(), value);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
        return 1;
    }

    @Override
    public int changeCheck(String username, String productId, String check) {
        String key = CART_KEY + username;
        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();
        Cart cart = hashOperations.get(key, productId);
        if (cart != null) {
            cart.setChecked(check);
            try {
                hashOperations.put(key,productId,cart);
                return 1;
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
        return 0;
    }
}
