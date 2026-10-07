package com.github.inzmamkhan.ziplink_backend.service.impl;

import com.github.inzmamkhan.ziplink_backend.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterServiceImpl implements RateLimiterService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${rate.limit.max-requests:10}")
    private int maxRequests;

    @Value("${rate.limit.window-seconds:60}")
    private int windowSeconds;

    @Override
    public boolean isAllowed(String clientIp) {
        String key = "rate_limit:" + clientIp;

        Long currentRequests = redisTemplate.opsForValue().increment(key);

        if (currentRequests != null && currentRequests == 1) {
            redisTemplate.expire(key, windowSeconds, TimeUnit.SECONDS);
        }

        return currentRequests != null && currentRequests <= maxRequests;
    }
}