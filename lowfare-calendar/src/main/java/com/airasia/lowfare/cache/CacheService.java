package com.airasia.lowfare.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CacheService {

    private final RedisTemplate<String, Object> redis;

    public CacheService(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

    public Object get(String key) {

        return redis.opsForValue().get(key);

    }

    public void put(String key,Object value) {

        redis.opsForValue().set(key, value, Duration.ofMinutes(30));

    }

    public void evict(String key) {

        redis.delete(key);

    }


}