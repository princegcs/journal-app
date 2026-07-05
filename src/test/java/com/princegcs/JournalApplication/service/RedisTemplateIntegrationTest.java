package com.princegcs.JournalApplication.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class RedisTemplateIntegrationTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Disabled
    @Test
    void storeAndGetValue() {

        redisTemplate.opsForValue().set("email", "gmail@email.com");

        String email = redisTemplate.opsForValue().get("email");

        assertEquals("gmail@email.com", email);
    }

    @Disabled
    @Test
    void expireKey() throws InterruptedException {

        redisTemplate.opsForValue().set("otp", "123456", Duration.ofSeconds(2));

        assertEquals("123456", redisTemplate.opsForValue().get("otp"));

        Thread.sleep(2500);

        assertNull(redisTemplate.opsForValue().get("otp"));
    }
}