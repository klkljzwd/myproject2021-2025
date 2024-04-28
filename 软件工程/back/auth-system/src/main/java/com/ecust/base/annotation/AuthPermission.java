package com.ecust.base.annotation;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPermission {
    String value() default "";
    int level() default 0;
}
