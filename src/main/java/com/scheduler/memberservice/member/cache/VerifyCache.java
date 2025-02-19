package com.scheduler.memberservice.member.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.MINUTES;

@Component
public class VerifyCache {

    Cache<String, String> orderCache = Caffeine.newBuilder()
            .expireAfterWrite(5, MINUTES)
            .maximumSize(100)
            .build();

    public void saveDirectOrder(String email, String authNum) {
        orderCache.put(email, authNum);
    }

    public String getAuthNumByEmail(String email) {

        if (orderCache.getIfPresent(email) == null) {
            throw new IllegalArgumentException("email not found with " + email);
        }

        return orderCache.getIfPresent(email);
    }

    public void invalidateAuthNum(String email) {
        orderCache.invalidate(email);
    }
}
