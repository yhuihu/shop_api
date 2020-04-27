package com.study.shop.business.controller.admin;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbUser;
import com.study.shop.provider.dto.AdminEditUserDTO;
import com.study.shop.provider.dto.AdminSearchDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tiger
 * @date 2020-03-29
 * @see com.study.shop.business.controller.admin
 **/
@RestController
@RequestMapping("admin-user")
public class UserController {
    @Reference(version = "1.0.0")
    private TbUserService tbUserService;

    /**
     * 控制台数据初始化
     *
     * @return ResponseResult
     */
    @GetMapping("init")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseResult getUserInit() {
        return new ResponseResult(ResponseResult.CodeStatus.OK);
    }

    /**
     * 管理员获取用户列表
     *
     * @param adminSearchDTO {@link AdminSearchDTO}
     * @return
     */
    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseResult adminGetUserList(AdminSearchDTO adminSearchDTO) {
        return new ResponseResult(ResponseResult.CodeStatus.OK, tbUserService.adminGetUser(adminSearchDTO));
    }

    @PutMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseResult adminEditUser(@RequestBody AdminEditUserDTO adminEditUserDTO) {
        TbUser byId = tbUserService.getById(Long.valueOf(adminEditUserDTO.getUserId()));
        if (!byId.getEmail().equals(adminEditUserDTO.getEmail())) {
            int byMail = tbUserService.getByMail(adminEditUserDTO.getEmail());
            if (byMail == 1) {
                return new ResponseResult(ResponseResult.CodeStatus.FAIL, "已存在该邮件对应用户");
            }
        }
        if (!byId.getPhone().equals(adminEditUserDTO.getPhone())) {
            int byPhone = tbUserService.findByPhone(adminEditUserDTO.getPhone());
            if (byPhone == 1) {
                return new ResponseResult(ResponseResult.CodeStatus.FAIL, "已存在该手机号对应用户");
            }
        }
        int i = tbUserService.adminEditUser(adminEditUserDTO);
        return new ResponseResult(i == 1 ? ResponseResult.CodeStatus.OK : ResponseResult.CodeStatus.FAIL);
    }
}
