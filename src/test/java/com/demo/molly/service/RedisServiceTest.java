package com.demo.molly.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataRedisTest
@EnabledIfEnvironmentVariable(named = "REDIS_PASSWORD", matches = ".+")
class RedisServiceTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void testRedisConnection() {
        String key = "test:redis:connection";
        String value = "connected";

        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(1));

        String actual = redisTemplate.opsForValue().get(key);
        assertEquals(value, actual);

        redisTemplate.delete(key);
        assertNull(redisTemplate.opsForValue().get(key));
    }
}
