package com.demo.molly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Token 黑名单服务
 */
@Service
public class TokenBlacklistService {

    private static final String ACCESS_TOKEN_BLACKLIST_PREFIX = "token:blacklist:access:";
    private static final String REFRESH_TOKEN_BLACKLIST_PREFIX = "token:blacklist:refresh:";

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklistAccessToken(String jti, long expirationSeconds) {
        if (expirationSeconds > 0) {
            redisTemplate.opsForValue().set(ACCESS_TOKEN_BLACKLIST_PREFIX + jti, "1", expirationSeconds, TimeUnit.SECONDS);
        }
    }

    public void blacklistRefreshToken(String jti, long expirationSeconds) {
        if (expirationSeconds > 0) {
            redisTemplate.opsForValue().set(REFRESH_TOKEN_BLACKLIST_PREFIX + jti, "1", expirationSeconds, TimeUnit.SECONDS);
        }
    }

    public boolean isAccessTokenBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(ACCESS_TOKEN_BLACKLIST_PREFIX + jti));
    }

    public boolean isRefreshTokenBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(REFRESH_TOKEN_BLACKLIST_PREFIX + jti));
    }
}
