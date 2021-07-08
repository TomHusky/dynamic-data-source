package com.lwj.demo.exception;

/**
 * @Description: 基类异常
 */
public class BaseException extends RuntimeException {
    private Integer code = ExceptionCode.OPERATE.getCode();

    public BaseException(String message) {
        super(message);
    }

    public BaseException(int code) {
        super("");
        this.code = code;
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
