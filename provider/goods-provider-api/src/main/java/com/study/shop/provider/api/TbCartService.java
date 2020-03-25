package com.study.shop.provider.api;

import com.study.shop.provider.domain.Cart;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Tiger
 * @date 2020-01-20
 * @see com.study.shop.provider.api
 **/
public interface TbCartService {
    /**
     * 功能描述: <br>
     *
     * @param list     数据集
     * @param username 用户名
     * @return 0 1
     */
    int addCart(List<Cart> list, String username);

    /**
     * 功能描述: <br>
     *
     * @param username 用户名
     * @return CartData {@link Cart}
     * @throws ExecutionException   e
     * @throws InterruptedException e
     */
    List<Cart> getAllCart(String username) throws ExecutionException, InterruptedException;

    /**
     * 删除购物车中的商品
     *
     * @param username  用户名
     * @param productId 商品编号
     * @return 0, 1
     */
    int deleteCart(String username, String productId);

    /**
     * 删除选中商品
     *
     * @param username 用户名
     * @return 0, 1
     */
    int deleteCheck(String username);

    /**
     * 选中所有商品
     *
     * @param username 用户名
     * @param check    修改为什么状态
     * @return 0, 1
     */
    int allCheck(String username, String check);

    /**
     * 更改商品选中信息
     *
     * @param username  用户名
     * @param productId 商品编号
     * @param check     选中状态
     * @return 0, 1
     */
    int changeCheck(String username, String productId, String check);
}
