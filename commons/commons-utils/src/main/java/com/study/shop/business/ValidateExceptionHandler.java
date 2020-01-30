package com.study.shop.business;

import com.study.shop.commons.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

/**
 * @author Tiger
 * @date 2020-01-27
 * @see com.study.shop.business
 **/
@Slf4j
@ControllerAdvice
public class ValidateExceptionHandler {
    /**
     * 处理请求中使用@Valid 验证请求实体校验失败后抛出的异常
     * @param e BindException
     * @return 异常处理
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity bindExceptionHandler(BindException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(setError(message), HttpStatus.OK);
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     * @param e ConstraintViolationException
     * @return 异常处理
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity constraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().iterator().next().getMessage();
        return new ResponseEntity<>(setError(message), HttpStatus.OK);
    }

    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
     * @param e MethodArgumentNotValidException
     * @return 异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message =e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(setError(message), HttpStatus.OK);
    }

    private ResponseResult setError(String message){
        return new ResponseResult(ExceptionStatus.VALID.getCode(),message);
    }
}
