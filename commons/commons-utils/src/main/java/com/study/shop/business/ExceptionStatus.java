package com.study.shop.business;

/**
 * @author Tiger
 * @date 2019-11-07
 * @see com.study.shop.business
 **/
public enum ExceptionStatus {
    /**
     * 图片内容为空
     */
    IMAGE_ERROR(100201, "图片内容为空"),
    /**
     * 拒绝的图片格式
     */
    TYPE_ERROR(100202, "拒绝的图片格式"),
    /**
     * 上传的图片过大
     */
    SIZE_ERROR(100203, "上传的图片过大"),
    /**
     * 账号密码错误
     */
    PASSWORD_ERROR(100101, "账号或密码错误"),

    /**
     * 账号被锁定
     */
    ACCOUNT_LOCK(100102, "账号被锁定"),

    /**
     * 账号不存在
     */
    ACCOUNT_NOT_EXIST(100103, "账户不存在"),

    /**
     * 数据库操作异常
     */
    DATABASE_ERROR(600101, "数据库操作异常"),

    /**
     * 数据库更新失败
     */
    UPDATE_ERROR(600102, "数据库更新失败"),

    /**
     * Redis操作异常
     */
    REDIS_ERROR(700101, "服务器操作异常"),

    /**
     * 未知错误
     */
    UNKNOWN(-1, "未知错误"),

    /**
     * 请求成功
     */
    OK(20000, "成功"),

    /**
     * 请求失败
     */
    FAIL(20001, "失败"),

    /**
     * 熔断请求
     */
    BREAKING(20002, "熔断"),

    /**
     * 参数错误
     */
    VALID(20003, "参数错误"),

    /**
     * 非法请求
     */
    ILLEGAL_REQUEST(50000, "非法请求"),

    /**
     * 非法令牌
     */
    ILLEGAL_TOKEN(50008, "非法令牌"),

    /**
     * 其他客户登录
     */
    OTHER_CLIENTS_LOGGED_IN(50012, "其他客户登录"),

    /**
     * 令牌已过期
     */
    TOKEN_EXPIRED(50014, "令牌已过期");

    private Integer code;
    private String message;

    ExceptionStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessage(int code) {
        for (ExceptionStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status.getMessage();
            }
        }
        return null;
    }
}
