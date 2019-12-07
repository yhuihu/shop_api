package com.study.shop.business.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.study.shop.business.utils.AlipayUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Tiger
 * @date 2019-11-29
 * @see com.study.shop.business.controller
 **/
@RestController
@Slf4j
public class test {
    @Resource
    AlipayUtils alipayUtils;

    @GetMapping("code")
    public String test(AlipayTradeWapPayModel model1) throws AlipayApiException {
        JSONObject data = new JSONObject();
        data.put("out_trade_no", "100001"); //商户订单号
        data.put("product_code", "FACE_TO_FACE_PAYMENT"); //当面付标识
        data.put("total_amount", "88.88"); //订单金额
        data.put("subject", "当面付"); //订单标题
        AlipayTradePrecreateRequest alipayTradePrecreateRequest = new AlipayTradePrecreateRequest();
        alipayTradePrecreateRequest.setBizContent(data.toJSONString());
        AlipayTradePrecreateResponse response = alipayUtils.getNormalClient().execute(alipayTradePrecreateRequest);
        log.info("阿里预支付创建结果：{}", response.getQrCode());
        return response.getQrCode();
    }

//    @GetMapping("test")
//    public String pc(AlipayTradeWapPayModel model1) throws AlipayApiException {
//        CertAlipayRequest certAlipayRequest = alipayUtils.getCertAlipayRequest();
//        //构造client
//        AlipayClient client = new DefaultAlipayClient(certAlipayRequest);
//        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
//        model.setOutTradeNo("100012");
//        model.setProductCode("FAST_INSTANT_TRADE_PAY");
//        model.setTotalAmount("188.88");
//        model.setSubject("Javen PC支付测试");
//        model.setBody("Javen IJPay PC支付测试");
//        String form = "";
//        try {
//            form = client.pageExecute(alipayUtils.getAlipayTradePagePayRequest(model)).getBody();
//        } catch (AlipayApiException e) {
//            log.error("支付宝支付异常:" + e.getErrMsg());
//        }
//        return form;
//    }

//    @GetMapping("toUser")
//    public String test1() throws AlipayApiException {
//        CertAlipayRequest certAlipayRequest = alipayUtils.getCertAlipayRequest();
//        AlipayClient client = new DefaultAlipayClient(certAlipayRequest);
//        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
//        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
//        model.setOutBizNo("100000");
//        model.setAmount("88.88");
//        model.setPayeeType("payee_type");
//        model.setPayeeAccount("rcgvnt0664@sandbox.com");
//        model.setPayeeRealName("payee_real_name");
//        model.setPayerShowName("payer_show_name");
//        model.setRemark("remark");
//        request.setBizModel(model);
//        AlipayFundTransToaccountTransferResponse response = client.execute(request);
//        return JSON.toJSONString(response);
//    }
}
