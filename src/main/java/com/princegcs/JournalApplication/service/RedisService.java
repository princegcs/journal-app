package com.princegcs.JournalApplication.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Service
@AllArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;



    public void set(String key, Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json);
        } catch (Exception e) {
            log.error("Failed to cache key {}", key, e);
        }
    }


    public void set(String key, Object value, Duration ttl){
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, ttl);
        } catch (Exception e) {
            log.error("Failed to cache key {}", key, e);
        }

    }

    public <T> T get(String key, Class<T> clazz){
        try{

            String json = redisTemplate.opsForValue().get(key);

            if( json == null ) {
                return null;
            }

            return objectMapper.readValue(json,clazz);

        }catch (Exception e){
            log.error("Failed to cache key {}", key, e);
            return null;
        }
    }


    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Failed to delete cache for key {}", key, e);
        }
    }

}
