package com.study.shop.provider.api;

import com.study.shop.provider.domain.TbUser;

/**
 * @author Admin
 */
public interface TbUserService {
    /**
     * 新增用户
     *
     * @param tuser {@link TbUser}
     * @return {@code int} 大于 0 则表示添加成功
     */
    int insert(TbUser tuser);

    /**
     * 获取用户
     *
     * @param username 用户名
     * @return {@link TbUser}
     */
    TbUser get(String username);

    /**
     * 获取用户
     *
     * @param tuser {@link TbUser}
     * @return {@link TbUser}
     */
    TbUser get(TbUser tuser);

    /**
     * 根据id获取用户
     * @param id 用户id
     * @return {@link TbUser}
     **/
    TbUser getById(int id);

    /**
     * 更新用户
     * <p>
     * 仅允许更新 邮箱、昵称、备注、状态
     * </p>
     *
     * @param tuser {@link TbUser}
     * @return {@code int} 大于 0 则表示更新成功
     */
    int update(TbUser tuser);

    /**
     * 修改密码
     *
     * @param username {@code String} 用户名
     * @param password {@code String} 明文密码
     * @return {@code int} 大于 0 则表示更新成功
     */
    int modifyPassword(String username, String password);

    /**
     * 修改头像
     *
     * @param username {@code String} 用户名
     * @param path     {@code String} 头像地址
     * @return {@code int} 大于 0 则表示更新成功
     */
    int modifyIcon(String username, String path);
}
