package com.study.shop.business.controller.admin;

import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbOrderService;
import com.study.shop.provider.domain.TbOrder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tiger
 * @date 2020-05-02
 * @see com.study.shop.business.controller.admin
 **/
@RestController
@RequestMapping("admin-order")
public class AdminOrderController {

    @Reference(version = "1.0.0")
    TbOrderService tbOrderService;

    @GetMapping("/init")
    public ResponseResult initData() {
        List<TbOrder> allOrder = tbOrderService.getAllOrder();
        Map<String, Object> map = new HashMap<>();
        map.put("count", allOrder.size());
        List<Integer> countValue = new ArrayList<>();
        List<Double> moneyValue = new ArrayList<>();
        Calendar cld = Calendar.getInstance(Locale.CHINA);
        cld.setFirstDayOfWeek(Calendar.MONDAY);
        List<TbOrder> orderList = getCount(allOrder, cld);
        setValue(countValue, moneyValue, orderList);
        cld.setFirstDayOfWeek(Calendar.TUESDAY);
        orderList = getCount(allOrder, cld);
        setValue(countValue, moneyValue, orderList);
        cld.setFirstDayOfWeek(Calendar.WEDNESDAY);
        orderList = getCount(allOrder, cld);
        setValue(countValue, moneyValue, orderList);
        cld.setFirstDayOfWeek(Calendar.THURSDAY);
        orderList = getCount(allOrder, cld);
        setValue(countValue, moneyValue, orderList);
        cld.setFirstDayOfWeek(Calendar.FRIDAY);
        orderList = getCount(allOrder, cld);
        setValue(countValue, moneyValue, orderList);
        cld.setFirstDayOfWeek(Calendar.SATURDAY);
        orderList = getCount(allOrder, cld);
        setValue(countValue, moneyValue, orderList);
        cld.setFirstDayOfWeek(Calendar.SUNDAY);
        orderList = getCount(allOrder, cld);
        setValue(countValue, moneyValue, orderList);
        map.put("weekOrderCount", countValue);
        map.put("weekMoneyCount", moneyValue);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, map);
    }

    public List<TbOrder> getCount(List<TbOrder> tbItems, Calendar calendar) {
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
        return tbItems.stream().filter((s) -> s.getEndTime().compareTo(dayStart) >= 0 && s.getEndTime().compareTo(dayEnd) <= 0).collect(Collectors.toList());
    }

    public void setValue(List<Integer> countValue, List<Double> moneyValue, List<TbOrder> tbOrders) {
        countValue.add(tbOrders.size());
        Double money = 0.00;
        for (TbOrder tbOrder : tbOrders) {
            money += tbOrder.getPayment();
        }
        moneyValue.add(money);
    }
}
