package com.study.shop.business.controller.admin;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbLogService;
import com.study.shop.provider.dto.AdminSearchDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tiger
 * @date 2020-04-18
 * @see com.study.shop.business.controller.admin
 **/
@RestController
@RequestMapping("admin-log")
public class LogController {
    @Reference(version = "1.0.0")
    TbLogService tbLogService;

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseResult adminGetLog(AdminSearchDTO adminSearchDTO) {
        return new ResponseResult(ResponseResult.CodeStatus.OK, tbLogService.getLogByPage(adminSearchDTO));
    }
}
