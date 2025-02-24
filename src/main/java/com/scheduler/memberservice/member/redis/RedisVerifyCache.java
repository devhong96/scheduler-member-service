package com.scheduler.memberservice.member.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import static java.util.concurrent.TimeUnit.SECONDS;

@Repository
@RequiredArgsConstructor
public class RedisVerifyCache {

    private static final String AUTH_NUM_PREFIX = "authNum:";
    private static final long EXPIRATION_TIME = 5 * 60; // 5분 (단위: 초)

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveAuthNum(String email, String authNum) {
        String key = AUTH_NUM_PREFIX + email;
        redisTemplate.opsForValue().set(key, authNum, EXPIRATION_TIME, SECONDS);
    }

    public String getAuthNumByEmail(String email) {
        String key = AUTH_NUM_PREFIX + email;
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void invalidateAuthNum(String email) {
        String key = AUTH_NUM_PREFIX + email;
        redisTemplate.delete(key);
    }
}