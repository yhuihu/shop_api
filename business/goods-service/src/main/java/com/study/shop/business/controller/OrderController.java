package com.study.shop.business.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.study.shop.commons.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tiger
 * @date 2020-02-13
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("order")
public class OrderController {

    @PostMapping()
    public ResponseResult createOrder(){

        return new ResponseResult<>(ResponseResult.CodeStatus.OK);
    }
    private static void generateQRCodeImage(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
    }
}
