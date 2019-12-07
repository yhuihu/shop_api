package com.study.shop.business.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Tiger
 * @date 2019-11-29
 * @see com.study.shop.business.config
 **/
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix="alipay")
public class AlipayConfiguration {
    /**
     * 商户appId
     */
    private String appId;

    /**
     * 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    private String notifyUrl;

    /**
     * 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
     */
    private String returnUrl;

    /**
     * 请求网关地址
     */
    private String gatewayUrl;

    /**
     * 编码
     */
    private String charset;

    /**
     * 返回格式
     */
    private String format;

    /**
     * RSA2  签名方式
     */
    private String signtype;
    /**
     * RSA私钥，用于对商户请求报文加签
     */
    private String appPrivateKey;
    /**
     * 支付宝RSA公钥，用于验签支付宝应答
     */
    private String alipayPublicKey;
    /**
     * alipay证书路径
     */
    private String alipayCert;

    /**
     * alipayRoot
     */
    private String alipayRoot;

    /**
     * alipay证书路径
     */
    private String appCert;
}
