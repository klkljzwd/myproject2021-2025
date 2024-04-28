package com.ecust.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RedisIdUtils {
    public static long nextId(RedisTemplate redisTemplate, String keyPreix){
            //1. 利用redis生成序列号
            Long increment = redisTemplate.opsForValue().increment("icr:" + keyPreix + ":");
            //3. 拼接并返回
        return 10000000+increment;
    }
}
