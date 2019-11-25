package com.study.shop.business;

import com.study.shop.commons.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Tiger
 * @date 2019-11-07
 * @see com.study.shop.business
 **/
@Slf4j
@ControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handlerException(HttpServletRequest request, Exception ex) {
        ResponseResult error = new ResponseResult();

        // 业务异常
        if (ex instanceof BusinessException) {
            error.setCode(((BusinessException) ex).getCode());
            error.setMessage(ex.getMessage());
            log.warn("[业务编码：{}异常记录：{}", error.getCode(), error.getMessage());
        }

        // 未知错误
        else {
            error.setCode(ExceptionStatus.UNKNOWN.getCode());
            error.setMessage(ExceptionStatus.UNKNOWN.getMessage());
        }

        return new ResponseEntity<>(error, HttpStatus.OK);
    }


}
