package com.study.shop.commons.aop.aspect;

import com.alibaba.fastjson.JSONObject;
import com.study.shop.business.BusinessException;
import com.study.shop.commons.aop.annotation.Log;
import com.study.shop.commons.aop.bean.SysLog;
import com.study.shop.commons.aop.utils.IPUtils;
import com.study.shop.utils.SnowIdUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author Tiger
 * @date 2020-04-17
 * @see com.study.shop.commons.aop.aspect
 **/
@Aspect
@Component
public class LogAspect {
    @Autowired
    DefaultMQProducer defaultMQProducer;

    @Pointcut("@annotation(com.study.shop.commons.aop.annotation.Log)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            result = point.proceed();
        } catch (BusinessException e) {
            throw e;
        } finally {
            // 执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            // 发送日志到mq
            sendLog(point, time);
        }
        return result;
    }

    private void sendLog(ProceedingJoinPoint joinPoint, long time) throws Exception {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = new SysLog();
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            // 注解上的描述
            sysLog.setName(logAnnotation.value());
        }
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                params.append("  ").append(paramNames[i]).append(": ").append(args[i]);
            }
            sysLog.setRequestParam(params.toString());
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        sysLog.setUrl(String.valueOf(request.getRequestURL()));
        // 设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));
        sysLog.setToken(request.getHeader("authorization"));
        sysLog.setRequestType(request.getMethod());
        sysLog.setTime((int) time);
        sysLog.setCreateDate(new Date());
        sysLog.setId(SnowIdUtils.uniqueLong());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("log", sysLog);
        // 发送日志
        defaultMQProducer.sendOneway(new Message("logTopic", "Logs", jsonObject.toJSONString().getBytes(RemotingHelper.DEFAULT_CHARSET)));
    }
}
