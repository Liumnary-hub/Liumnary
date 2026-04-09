package com.example.api.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int limit() default 5;    // 最多请求次数
    int expire() default 60; // 秒（默认60秒内最多5次）
}