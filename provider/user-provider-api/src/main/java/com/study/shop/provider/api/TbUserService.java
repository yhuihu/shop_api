package com.study.shop.provider.api;

import com.github.pagehelper.PageInfo;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.dto.AdminEditUserDTO;
import com.study.shop.provider.dto.AdminSearchDTO;
import com.study.shop.provider.vo.AdminUserVO;

/**
 * @author Admin
 */
public interface TbUserService {
    /**
     * 新增用户
     *
     * @param tuser {@link TbUser}
     * @return {@code int} 大于 0 则表示添加成功  -1手机号已存在
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
     *
     * @param id 用户id
     * @return {@link TbUser}
     **/
    TbUser getById(Long id);

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

    /**
     * 根据邮箱查找用户
     *
     * @param email e
     * @return 0-不存在,1-存在
     */
    int getByMail(String email);

    /**
     * 根据手机号查找用户
     * @param phone p
     * @return 0-不存在 1-存在
     */
    int findByPhone(String phone);
    /**
     * 找回密码
     *
     * @param email    邮箱
     * @param password 密码
     * @return 0，失败  1，成功
     */
    int findUser(String email, String password);


    /**
     * 管理员获取用户列表
     *
     * @param adminSearchDTO {@link AdminSearchDTO}
     * @return {@link AdminUserVO}
     */
    PageInfo<AdminUserVO> adminGetUser(AdminSearchDTO adminSearchDTO);

    /**
     * 管理员修改用户信息
     * @param adminEditUserDTO {@link AdminEditUserDTO}
     * @return 0,1
     */
    int adminEditUser(AdminEditUserDTO adminEditUserDTO);
}
