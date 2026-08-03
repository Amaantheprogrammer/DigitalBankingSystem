package com.MyProject.DigitalBankingSystem.idempotency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final StringRedisTemplate redisTemplate;
    private final static Duration TTL = Duration.ofHours(24);

    private String buildKey(String key, String operation) {
        return "idempotency:" + operation + ":" + key;
    }

    public void save(String key, String operation, String transactionReference) {
        redisTemplate.opsForValue().set(
                buildKey(key, operation),
                transactionReference,
                TTL
        );
    }

    public String get(String key, String operation) {
        return redisTemplate.opsForValue().get(buildKey(key, operation));
    }

    public boolean exists(String key, String operation) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(buildKey(key, operation)));
    }

}
