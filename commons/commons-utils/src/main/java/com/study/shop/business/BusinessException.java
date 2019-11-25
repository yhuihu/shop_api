package com.study.shop.business;

/**
 * @author Tiger
 * @date 2019-11-07
 * @see com.study.shop.business
 **/
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -5691472265585910797L;
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BusinessException() {

    }

    public BusinessException(ExceptionStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }
}
