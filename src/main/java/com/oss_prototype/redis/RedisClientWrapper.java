package com.oss_prototype.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisClientWrapper {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setValue(final String key, final String value, final long ttlSec) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, Duration.ofSeconds(ttlSec));
    }

    public String getValue(final String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }
}
