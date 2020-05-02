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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        List<TbUser> allUser = tbUserService.getAllUser();
        Map<String, Object> map = new HashMap<>();
        map.put("count", allUser.size());
        Calendar cld = Calendar.getInstance(Locale.CHINA);
        cld.setFirstDayOfWeek(Calendar.MONDAY);
        List<Integer> list = new ArrayList<>();
        list.add(getCount(allUser, cld));
        cld.setFirstDayOfWeek(Calendar.TUESDAY);
        list.add(getCount(allUser, cld));
        cld.setFirstDayOfWeek(Calendar.WEDNESDAY);
        list.add(getCount(allUser, cld));
        cld.setFirstDayOfWeek(Calendar.THURSDAY);
        list.add(getCount(allUser, cld));
        cld.setFirstDayOfWeek(Calendar.FRIDAY);
        list.add(getCount(allUser, cld));
        cld.setFirstDayOfWeek(Calendar.SATURDAY);
        list.add(getCount(allUser, cld));
        cld.setFirstDayOfWeek(Calendar.SUNDAY);
        list.add(getCount(allUser, cld));
        map.put("weekUserCount", list);
        return new ResponseResult(ResponseResult.CodeStatus.OK, map);
    }

    public Integer getCount(List<TbUser> allUser, Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dayStart = calendar.getTime();
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date dayEnd = calendar.getTime();
        return Long.valueOf(allUser.stream().filter((s) -> s.getCreateTime().compareTo(dayStart) >= 0 && s.getCreateTime().compareTo(dayEnd) <= 0).count()).intValue();
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
