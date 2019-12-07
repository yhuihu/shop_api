package com.study.shop.business.utils;

import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.study.shop.business.config.AlipayConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Tiger
 * @date 2019-11-30
 * @see com.study.shop.business.utils
 **/
@Component
public class AlipayUtils {
    @Resource
    private AlipayConfiguration alipayConfiguration;

    public CertAlipayRequest getCertAlipayRequest() {
        //构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //设置网关地址
        certAlipayRequest.setServerUrl(alipayConfiguration.getGatewayUrl());
        //设置应用Id
        certAlipayRequest.setAppId(alipayConfiguration.getAppId());
        //设置应用私钥
        certAlipayRequest.setPrivateKey(alipayConfiguration.getAppPrivateKey());
        //设置请求格式，固定值json
        certAlipayRequest.setFormat(alipayConfiguration.getFormat());
        //设置字符集
        certAlipayRequest.setCharset(alipayConfiguration.getCharset());
        //设置签名类型
        certAlipayRequest.setSignType(alipayConfiguration.getSigntype());
        //设置应用公钥证书路径
        certAlipayRequest.setCertPath(alipayConfiguration.getAppCert());
        //设置支付宝公钥证书路径
        certAlipayRequest.setAlipayPublicCertPath(alipayConfiguration.getAlipayCert());
        //设置支付宝根证书路径
        certAlipayRequest.setRootCertPath(alipayConfiguration.getAlipayRoot());
        return certAlipayRequest;
    }

    public AlipayClient getNormalClient(){
        String zfbPublicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzdknbgMbM7Bxs+/47EYle4N7qzXGizBQvprMR+8IHuxbFWRjqFNEVA7j8MUYTe+fE4JNT6XWDDl8assP/QKUA4/EBVtnmXv7ASeZR262wpW0aF8UpS7b2mwtcEoMOj26y6UVOFgbgT+8ynnW88MBXX/mUcZ11ohIdVJT6yJwRMCLfqKR0FaJMiv+lW4Dsp83vO7kpSJNdwVreUaeuCZ9KM8swAqh1vRfuBqT5PJD8G6TEhvJQBWGH7lzapaJ6Kfh91HEBMCEzqA1/np5Z02pdH0D81pGOtUSTbjwHlOQnge8W42fucF/EGQDQ/ZQpwWziGMfwOaGswwwMrzKs7DmeQIDAQAB";
        String privateKey="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZH7PdQbuM1sYHPB/0kECYB3l7L2wzK8BgxmKwBKB6IJfwEHxXOjO6JCqr4UakfD0Jb10V1TRaIgLSnIy13JWxHC5NhNrIWEGRdQwCBBA1EJLcq6d+JjDyU1FWl2kULHZ949Lk0wkuPz/5tLfT/pulDgRJw334bjjB1C+GXSE+6xRCC/huuyLm59voyQIGJIr1+2NlySGFxYGifm0QtoNMHWVPVqtTGWLF0weVHtrVtoONw8tKE4HuCid6/fmfyvaAthFtKAMaFBtd8isUkrvmMyFzFansqt3qe8a4y9+Hxio1XJPM+HA53pFMaBjfOyc/6gKnDm27p2X/mGp7B/M9AgMBAAECggEADzg5dnp8G8mVMU7mljZoVTQY/Qd/p65nlrbhREGRedIXiAL7eOgOEChcHW1syhiXMILjK7JfW74ZrblluwXL0yjL6WSVImizS7Pol6KzwbJ+BnUBVuaQ4uUpJoQyK5EnzeeBJMFdHj2nLDisNOQPsF4uSu6lbLX36jwe5SoIjepEAEs7olL3wcawVENiJZMumPMGpM61CVDc50rSPWh+P4bRwoF4yUh/9Jx+OCF03H8FHUwPXsHoo0v0JOnG5+Gah0aQ3hQ8ertN7TxoP7h/b05dU+/1OXbD3+H6kLnPmEzkPvCOXV0ay+dHehPXkmJFDDqhumJ9gvm7xkgBHp6JwQKBgQDjxsXOUT4n/CPGLshcR17IMYfbFfoR23zKHZ6Waq+IheIqITRilQFmsGd0R7aSMPa3J+9vBBkbdemIZKIFnJvXbMHDQEcK/NLlJVeoJhJpdvjreCJnn4a5ZSbOaNd1vUxZcotH2FV3o+VPd+Fp1Auo1FqLXL9v7ScXUqINpdsxOQKBgQCsGOWliNUCRrXaqR1mHsT30TMElbWt9uxGVDtY4S8oXI0U7bZQzwVOzkUHXrZLZXseDPcnbAyAhF2jD9anoHFJVIU878Nkx6r7P/qUsLE+jnB/ndZd/cFyd2zs4zx6DoY2edhl5ZD3IaSM/zuOVlVSntH2lQxr3hf5yYC42UaGJQKBgQCw/7KQi+NgEPf/tjTuOGu3531pyx8jJ0tGiFPHESpMTbWdBWZ+wMlw8d3Cp0OBQNlHJJpTy3xzMWKDIagh8jvL4HCFeHp8WFDNzgHa4ZLnL5No71bU7Us04BySwzfC/5HH5FHFR5/Ks6qq4T5iDWvuCu49Y5pDsKby4byaHGtDSQKBgFw13t5KKa/ViV0G3hy4gdsyuT5MpRL4SfMI8N+XkHf01xcOo3A67GIkPXJMeip9znD0i30Q9MXrpmxXcTFR00FySw2yfiRCo8eJqU8AOYgGNBr8zD6CV6Nof/GRc/cGTz1jHeLDFWCFIzD1FMkYUxE4zdFsXUJytq/9Gr5wlMwNAoGBAJ+R7InBI3FYo1LjwIM5434QKggBXsvpYFMPmHwC3TrfI2wSA4h8RYO9AL2q5yL+m52sgR+8TB7PqmHfHw3H19tS2WOKwgyWOTT7bCt5AUmakAnzrp2q3rtJQCX1eqwLMptqEKLWaRmoqrOmALgdOPEw2pxln3xyS6YVHUQ2spnj";
        return new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do","2016101600699529",privateKey,"json","utf-8",zfbPublicKey,"RSA2");
    }

    public AlipayTradePagePayRequest getAlipayTradePagePayRequest(AlipayTradeWapPayModel model){
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //设置回调地址
        alipayRequest.setReturnUrl(alipayConfiguration.getReturnUrl());
        alipayRequest.setNotifyUrl(alipayConfiguration.getNotifyUrl());
        alipayRequest.setBizModel(model);
        return alipayRequest;
    }
}
