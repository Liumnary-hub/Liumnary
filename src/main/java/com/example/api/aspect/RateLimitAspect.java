package com.example.api.aspect;


import com.example.api.annotation.RateLimit;
import com.example.api.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RateLimitAspect {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 匹配所有加了 @RateLimit 注解的方法
    @Pointcut("@annotation(com.example.api.annotation.RateLimit)")
    public void rateLimitPointCut() {}

    @Around("rateLimitPointCut() && @annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {

        System.out.println("==================== 限流生效啦！！！====================");

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteAddr();

        String key = "rate:limit:" + ip;

        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            redisTemplate.expire(key, rateLimit.expire(), TimeUnit.SECONDS);
        }

        if (count > rateLimit.limit()) {
            throw new BusinessException("操作频繁，请稍后再试");
        }

        return joinPoint.proceed();
    }
}