package com.ecust.utils;
import com.ecust.base.constant.Constant;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.Set;

public class FlushUser {
    public static void flush(StringRedisTemplate stringRedisTemplate){
        Set<String> keys = stringRedisTemplate.keys(Constant.userPrix+"*");
        for(String key:keys){
            if(key.equals(Constant.userPrix+"1")){
                continue;
            }
            stringRedisTemplate.delete(key);
        }
    }
    public static void flush(StringRedisTemplate stringRedisTemplate,String keyPattern){
        stringRedisTemplate.delete(keyPattern);
    }
}
