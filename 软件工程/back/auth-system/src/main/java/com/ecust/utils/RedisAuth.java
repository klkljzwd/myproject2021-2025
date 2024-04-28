package com.ecust.utils;

import cn.hutool.json.JSONUtil;
import com.ecust.base.constant.Constant;
import com.ecust.base.entity.Authority;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


public class RedisAuth {
    public static boolean getAuth(StringRedisTemplate redisTemplate, Long id,Integer level){
        String s = redisTemplate.opsForValue().get(Constant.userPrix + id);
        List<Authority> authorities = JSONUtil.toList(s, Authority.class);
        for(Authority authority:authorities){
            if(authority.getLevel()<=level){
                return true;
            }
        }
        return false;
    }
}
