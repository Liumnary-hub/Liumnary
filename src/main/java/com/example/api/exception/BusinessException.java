package com.example.api.exception;

/**
 * 自定义业务异常
 * 用于接口抛出友好提示
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

}