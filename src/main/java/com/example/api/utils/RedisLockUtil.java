package com.example.api.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Component
public class RedisLockUtil {

    private static final Long SUCCESS = 1L;

    // 加锁
    public boolean lock(StringRedisTemplate redisTemplate, String key, String value, long expireSeconds) {
        String script = "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), value, String.valueOf(expireSeconds));
        return SUCCESS.equals(result);
    }

    // 解锁
    public boolean unlock(StringRedisTemplate redisTemplate, String key, String value) {
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), value);
        return SUCCESS.equals(result);
    }
}