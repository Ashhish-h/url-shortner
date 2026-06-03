package com.url_shortner.url_shortner.cache.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisCahceServiceImpl {

    @Value("${redis.lock.ttl.seconds}")
    private Integer redisLockTTlSeconds;

    @Value("${redis.default.ttl.seconds}")
    private Integer redisDefaultTTlSeconds;

    private final RedisTemplate<String, String> redisTemplate;

}
