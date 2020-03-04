package com.study.shop.business.controller;

import com.qiniu.util.Auth;
import com.study.shop.commons.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tiger
 * @date 2020-03-03
 * @see com.study.shop.business.controller
 **/
@Slf4j
@RestController
@RequestMapping("upload")
public class UploadController {
    private static final String ACCESSKEY = "";
    private static final String SECRET_KEY = "";
    private static final String BUCKET = "";

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping()
    public ResponseResult uploadToken(){
        Auth auth = Auth.create(ACCESSKEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,upToken);
    }
}
