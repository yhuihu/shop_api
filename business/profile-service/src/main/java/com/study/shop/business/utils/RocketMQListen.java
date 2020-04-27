package com.study.shop.business.utils;

import com.alibaba.fastjson.JSONObject;
import com.study.shop.commons.aop.bean.SysLog;
import com.study.shop.provider.api.TbLogService;
import com.study.shop.provider.domain.TbLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Tiger
 * @date 2020-04-17
 * @see com.study.shop.business.utils
 **/
@Component
@Slf4j
public class RocketMQListen {
    @Reference(version = "1.0.0")
    TbLogService tbLogService;
    @Resource
    public TokenStore tokenStore;

    @StreamListener(LogInput.INPUT)
    public void orderTransaction(@Payload String receiveMsg) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(receiveMsg);
        SysLog sysLog = JSONObject.parseObject(jsonObject.getString("log"), SysLog.class);
        TbLog tbLog = new TbLog();
        BeanUtils.copyProperties(sysLog, tbLog);
//        if (sysLog.getToken() != null) {
//            OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(sysLog.getToken());
//            tbLog.setName(oAuth2Authentication.getName());
//        }
        int i = tbLogService.insertLog(tbLog);
        if (i == 0) {
            throw new Exception();
        }
    }
}
