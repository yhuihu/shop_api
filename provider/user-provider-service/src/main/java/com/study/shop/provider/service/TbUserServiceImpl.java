package com.study.shop.provider.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.github.pagehelper.PageInfo;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.dto.AdminEditUserDTO;
import com.study.shop.provider.dto.AdminSearchDTO;
import com.study.shop.provider.mapper.TbUserMapper;
import com.study.shop.provider.service.fallback.TbUserServiceFallback;
import com.study.shop.provider.vo.AdminUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * @author Admin
 */
@Slf4j
@Service(version = "1.0.0", parameters = {"insert.retries", "0", "insert.timeout", "60000"})
public class TbUserServiceImpl implements TbUserService {

    @Resource
    private TbUserMapper tbUserMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public int insert(TbUser tuser) {
        Example example = new Example(TbUser.class);
        example.createCriteria().andEqualTo("phone", tuser.getPhone());
        TbUser tbUser = tbUserMapper.selectOneByExample(example);
        if (tbUser != null) {
            return -1;
        }
        // 初始化用户对象
        initUmsAdmin(tuser);
        return tbUserMapper.insert(tuser);
    }

    /**
     * 熔断器的使用
     *
     * <p>
     * 1.  {@link SentinelResource#value()} 对应的是 Sentinel 控制台中的资源，可用作控制台设置【流控】和【降级】操作 <br>
     * 2.  {@link SentinelResource#fallback()} 对应的是 {@link TbUserServiceFallback#getByUsernameFallback(String, Throwable)}，并且必须为 `static` <br>
     * 3. 如果不设置 {@link SentinelResource#fallbackClass()}，则需要在当前类中创建一个 `Fallback` 函数，函数签名与原函数一致或加一个 {@link Throwable} 类型的参数
     * </p>
     *
     * @param username {@code String} 用户名
     * @return {@link TbUser}
     */
    @Override
    @SentinelResource(value = "getByUsername", fallback = "getByUsernameFallback", fallbackClass = TbUserServiceFallback.class)
    public TbUser get(String username) {
        Example example = new Example(TbUser.class);
        example.createCriteria().andEqualTo("username", username);
        return tbUserMapper.selectOneByExample(example);
    }

    @Override
    public TbUser get(TbUser tuser) {
        return tbUserMapper.selectOne(tuser);
    }

    @Override
    public TbUser getById(Long id) {
        Example example = new Example(TbUser.class);
        example.createCriteria().andEqualTo("id", id);
        return tbUserMapper.selectOneByExample(example);
    }

    @Override
    public int update(TbUser tuser) {
        // 获取原始用户信息
        return tbUserMapper.updateByPrimaryKey(tuser);
    }

    @Override
    public int modifyPassword(String username, String password) {
        TbUser tuser = get(username);
        tuser.setPassword(passwordEncoder.encode(password));
        return tbUserMapper.updateByPrimaryKey(tuser);
    }

    @Override
    public int modifyIcon(String username, String path) {
        TbUser tuser = get(username);
        tuser.setIcon(path);
        return tbUserMapper.updateByPrimaryKey(tuser);
    }

    @Override
    public int getByMail(String email) {
        Example example = new Example(TbUser.class);
        example.createCriteria().andEqualTo("email", email);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if (tbUsers.size() > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public int findByPhone(String phone) {
        Example example = new Example(TbUser.class);
        example.createCriteria().andEqualTo("phone", phone);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if (tbUsers.size() > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public int findUser(String email, String password) {
        Example example = new Example(TbUser.class);
        example.createCriteria().andEqualTo("email", email);
        TbUser tbUser = tbUserMapper.selectOneByExample(example);
        tbUser.setPassword(passwordEncoder.encode(password));
        try {
            tbUserMapper.updateByPrimaryKeySelective(tbUser);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public PageInfo<AdminUserVO> adminGetUser(AdminSearchDTO adminSearchDTO) {
        TbLogServiceImpl.initPageParams(adminSearchDTO);
        List<AdminUserVO> list = tbUserMapper.adminGetUser(adminSearchDTO);
        return new PageInfo<>(list);
    }

    @Override
    public int adminEditUser(AdminEditUserDTO adminEditUserDTO) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(Integer.valueOf(adminEditUserDTO.getUserId()));
        tbUser.setPhone(adminEditUserDTO.getPhone());
        tbUser.setEmail(adminEditUserDTO.getEmail());
        tbUser.setStatus(adminEditUserDTO.getStatus());
        try {
            return tbUserMapper.updateByPrimaryKeySelective(tbUser);
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public List<TbUser> getAllUser() {
        return tbUserMapper.selectAll();
    }

    private void initUmsAdmin(TbUser tuser) {
        // 初始化创建时间
        tuser.setCreateTime(new Date());
        tuser.setUpdateTime(new Date());
        // 初始化状态
        if (tuser.getStatus() == null) {
            tuser.setStatus(1);
        }
        tuser.setIcon("http://yhhu.xyz/ft1Uo1DThKrCsVGosrEVxuGK");
        tuser.setRole("USER");
        // 密码加密
        tuser.setPassword(passwordEncoder.encode(tuser.getPassword()));
    }

}
