package com.study.shop.cloud.controller;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.study.shop.business.BusinessException;
import com.study.shop.business.ExceptionStatus;
import com.study.shop.cloud.dto.FileInfo;
import com.study.shop.commons.dto.ResponseResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.cloud.controller
 **/
@RestController
@RequestMapping(value = "upload")
public class UploadController {
    private static final String ACCESSKEY = "";
    private static final String SECRET_KEY = "";
    private static final String BUCKET = "";
    private static final String DOMAIN = "";
    private static final List<String> ACCEPT_TYPE = new ArrayList<>(Arrays.asList("jpg", "jpeg", "heic", "png"));

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
        // 构造一个带指定Zone对象的配置类, 注意这里的Zone.zone0需要根据主机选择
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(ACCESSKEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = uploadManager.put(multipartFile.getInputStream(),
                    UUID.randomUUID().toString().replace("-", ""),
                    upToken, null, null);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            // 这个returnPath是获得到的外链地址,通过这个地址可以直接打开图片
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "文件上传成功", new FileInfo(DOMAIN + "/" + putRet.key));
        } catch (QiniuException ex) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "文件上传失败，请重试");
        }
    }
}
