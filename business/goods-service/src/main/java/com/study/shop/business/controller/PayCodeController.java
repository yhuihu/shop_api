package com.study.shop.business.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tiger
 * @date 2020-02-13
 * @see com.study.shop.business.controller
 **/
@RestController
public class PayCodeController {
    private static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public String getCode(String appointmentNumber) {
        String ctxPath = "D://file";
        String fileName = "twoCode.png";
        String bizPath = "files";
        String qrCode = "files";
        try {
            String nowTime = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowTime);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            String savePath = file.getPath() + File.separator + fileName;
            qrCode = bizPath + File.separator + nowTime + File.separator + fileName;
            if (savePath.contains("\\")) {
                savePath = savePath.replace("\\", "/");
            }
            if (qrCode.contains("\\")) {
                qrCode = qrCode.replace("\\", "/");
            }
            System.out.print(qrCode);
            System.out.print(savePath);
            generateQRCodeImage(appointmentNumber, 350, 350, savePath);
            return qrCode;
        } catch (Exception e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        }
        return qrCode;
    }
}
