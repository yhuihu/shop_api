package com.study.shop.business.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.study.shop.commons.dto.ResponseResult;
import com.study.shop.provider.api.TbUserService;
import com.study.shop.provider.domain.TbUser;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Tiger
 * @date 2020-02-13
 * @see com.study.shop.business.controller
 **/
@RestController
@RequestMapping("order")
public class OrderController {
    @Reference(version = "1.0.0")
    private TbUserService tbUserService;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 12, 5, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    @PostMapping()
    public ResponseResult<String> createOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Future<TbUser> userSubmit = threadPoolExecutor.submit(() -> tbUserService.get(username));
        try {
            TbUser tbUser = userSubmit.get();
            return new ResponseResult<>(ResponseResult.CodeStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL);
        }
    }

    private static void generateQRCodeImage(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
    }
}
