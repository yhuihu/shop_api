package com.study.shop.cloud.controller;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.study.shop.cloud.dto.FileInfo;
import com.study.shop.commons.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Tiger
 * @date 2019-09-16
 * @see com.study.shop.cloud.controller
 **/
@RestController
@RequestMapping(value = "upload")
public class UploadController {
    private static final String ACCESSKEY = "WFu4RDKywdcDlQCYJhcBaQh0uZlHik79PFo3WCRh";
    private static final String SECRET_KEY = "Zx3CSdhhJZMIetxUiRp2rBqCtgSp7Qf6Sb8EiseP";
    private static final String BUCKET = "shop-cloud";
    private static final String DOMAIN = "http://cloud.yhhu.xyz";

    /**
     * 将图片上传到七牛云
     */
    @PostMapping(value = "")
    public ResponseResult<FileInfo> uploadFile(MultipartFile multipartFile) throws NullPointerException, IOException {
        // 构造一个带指定Zone对象的配置类, 注意这里的Zone.zone0需要根据主机选择
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(ACCESSKEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = uploadManager.put((FileInputStream) multipartFile.getInputStream(),
                    UUID.randomUUID().toString().replace("-", ""),
                    upToken, null, null);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            // 这个returnPath是获得到的外链地址,通过这个地址可以直接打开图片
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "文件上传成功", new FileInfo(DOMAIN + "/" + putRet.key));
        } catch (QiniuException ex) {
            return new ResponseResult<FileInfo>(ResponseResult.CodeStatus.FAIL, "文件上传失败，请重试");
        }
    }
}
