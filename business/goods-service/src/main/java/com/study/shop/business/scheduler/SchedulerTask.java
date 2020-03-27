package com.study.shop.business.scheduler;

import com.study.shop.provider.api.TbItemService;
import com.study.shop.provider.api.TbOrderService;
import com.study.shop.provider.domain.TbOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Tiger
 * @date 2020-03-20
 * @see com.study.shop.business.scheduler
 **/
@Component
@Slf4j
public class SchedulerTask {

    @Reference(version = "1.0.0")
    private TbOrderService tbOrderService;
    @Reference(version = "1.0.0")
    private TbItemService tbItemService;
    @Autowired
    RedisTemplate redisTemplate;

    @Scheduled(cron = "0 */10 * * * ?")
    public void orderScheduler() {
        try {
            Calendar c = Calendar.getInstance();
            Date date = new Date();
            c.setTime(date);
            c.add(Calendar.MINUTE, -30);
            Date time = c.getTime();
            Boolean orderScheduler;
//            防止多机器部署时发生重复执行问题
            orderScheduler = redisTemplate.opsForValue().setIfAbsent("ORDER_SCHEDULER", time, 5, TimeUnit.MINUTES);
            if (orderScheduler != null && orderScheduler) {
                List<TbOrder> timeOutOrder = tbOrderService.getTimeOutOrder(time);
                List<Long> goodsList = new ArrayList<>();
                HashSet<Long> hashSetGroups = new HashSet<>();
                timeOutOrder.forEach(item -> {
                    goodsList.add(item.getGoodsId());
                    hashSetGroups.add(item.getGroupId());
                });
                hashSetGroups.forEach(item -> {
                    tbOrderService.changeOrderStatus(item, 5); //修改订单状态为关闭
//                    tbOrderService.deleteByGroup(item);  直接删除订单
                });
                tbItemService.changeGoodsStatus(goodsList, 1);
            }
        } catch (Exception e) {
            log.error("定时排查未付款订单失败！,{}", e.getMessage());
        }
    }
}
