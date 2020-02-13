package com.study.shop.business;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author Tiger
 * @date 2020-02-13
 * @see com.study.shop.business
 **/
public class UploadUtils {
    private static final String ACCESSKEY = "";
    private static final String SECRET_KEY = "";
    private static final String BUCKET = "";
    private static final String DOMAIN = "";

    public static String upload(InputStream inputStream) throws IOException {
        // 构造一个带指定Zone对象的配置类, 注意这里的Zone.zone0需要根据主机选择
        Configuration cfg = new Configuration(Region.region2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(ACCESSKEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = uploadManager.put(inputStream,
                    UUID.randomUUID().toString().replace("-", ""),
                    upToken, null, null);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return DOMAIN + "/" + putRet.key;
        } catch (QiniuException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
