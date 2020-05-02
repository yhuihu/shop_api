package com.study.shop.business.controller.admin;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.domain.TbItem;
import com.study.shop.provider.dto.AdminSearchGoodsDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 * @date 2020-04-18
 * @see com.study.shop.business.controller.admin
 **/
@RestController
@RequestMapping("admin-goods")
public class AdminGoodsController {
    @Reference(version = "1.0.0")
    TbItemService tbItemService;

    @GetMapping("/init")
    public ResponseResult initData() {
        List<TbItem> allItem = tbItemService.getAllItem();
        Map<String, Object> map = new HashMap<>();
        map.put("count", allItem.size());
        Calendar cld = Calendar.getInstance(Locale.CHINA);
        cld.setFirstDayOfWeek(Calendar.MONDAY);
        List<Integer> list = new ArrayList<>();
        list.add(getCount(allItem, cld));
        cld.setFirstDayOfWeek(Calendar.TUESDAY);
        list.add(getCount(allItem, cld));
        cld.setFirstDayOfWeek(Calendar.WEDNESDAY);
        list.add(getCount(allItem, cld));
        cld.setFirstDayOfWeek(Calendar.THURSDAY);
        list.add(getCount(allItem, cld));
        cld.setFirstDayOfWeek(Calendar.FRIDAY);
        list.add(getCount(allItem, cld));
        cld.setFirstDayOfWeek(Calendar.SATURDAY);
        list.add(getCount(allItem, cld));
        cld.setFirstDayOfWeek(Calendar.SUNDAY);
        list.add(getCount(allItem, cld));
        map.put("weekGoodsCount", list);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, map);
    }

    public Integer getCount(List<TbItem> tbItems, Calendar calendar) {
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
        return Long.valueOf(tbItems.stream().filter((s) -> s.getCreated().compareTo(dayStart) >= 0 && s.getCreated().compareTo(dayEnd) <= 0).count()).intValue();
    }

    /**
     * 管理员获取商品列表
     *
     * @param adminSearchGoodsDTO dto
     * @return ResponseResult
     */
    @GetMapping()
    public ResponseResult adminGetGoodsList(AdminSearchGoodsDTO adminSearchGoodsDTO) {
        return new ResponseResult(ResponseResult.CodeStatus.OK, tbItemService.adminGetGoodsListByPage(adminSearchGoodsDTO));
    }

    /**
     * 删除商品
     *
     * @param id 编号
     * @return ResponseResult
     */
    @DeleteMapping("/{id}")
    public ResponseResult adminDeleteGoods(@PathVariable(name = "id") String id) {
        return new ResponseResult(ResponseResult.CodeStatus.OK, tbItemService.adminDeleteGoods(Long.valueOf(id)));
    }
}
