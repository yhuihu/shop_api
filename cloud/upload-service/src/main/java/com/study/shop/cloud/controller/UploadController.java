package com.study.shop.cloud.controller;

import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.business.UploadUtils;
import com.study.shop.cloud.dto.FileInfo;
import com.study.shop.commons.dto.ResponseResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.cloud.controller
 **/
@RestController
@RequestMapping(value = "upload")
public class UploadController {
    private static final List<String> ACCEPT_TYPE = new ArrayList<>(Arrays.asList("jpg", "jpeg", "heic", "png", "bmp"));

    /**
     * 将图片上传到七牛云
     */
    @PostMapping()
    public ResponseResult uploadFile(@RequestParam(value = "file", required = true) MultipartFile multipartFile) throws NullPointerException, IOException {
        if (multipartFile == null) {
            throw new BusinessException(ExceptionStatus.IMAGE_ERROR);
        }
        String suffix = StringUtils.unqualify(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        if (!ACCEPT_TYPE.contains(suffix)) {
            throw new BusinessException(ExceptionStatus.TYPE_ERROR);
        }
        if(!checkFileSize(multipartFile.getSize(),5,"M")){
            throw new BusinessException(ExceptionStatus.SIZE_ERROR);
        }
        InputStream inputStream = multipartFile.getInputStream();
        String path= UploadUtils.upload(inputStream);
        if(path!=null){
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "文件上传成功", new FileInfo(path));
        }else{
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "文件上传失败，请重试");
        }
    }

    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        switch (unit.toUpperCase()) {
            case "B":
                fileSize = (double) len;
                break;
            case "K":
                fileSize = (double) len / 1024;
                break;
            case "M":
                fileSize = (double) len / 1048576;
                break;
            case "G":
                fileSize = (double) len / 1073741824;
                break;
            default:
                break;
        }
        return (fileSize < size);
    }
}
