package com.ecust.base.aop;

import cn.hutool.json.JSONUtil;
import com.ecust.auth.advicer.NormalException;
import com.ecust.auth.advicer.UnauthorizationException;
import com.ecust.base.ThreadLocalAuth;
import com.ecust.base.annotation.AuthPermission;
import com.ecust.base.constant.Constant;
import com.ecust.base.entity.Authority;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class AuthorityPermission {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Pointcut("execution(* com.ecust.auth.service.impl.*.*(..)) " +
            "&& @annotation(com.ecust.base.annotation.AuthPermission)" +
            "&& !within(com.ecust.auth.service.impl.LoginServiceImpl)")
    public void permissionPointCut(){

    }

    @Before("permissionPointCut()")
    public void permission(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AuthPermission authPermission = signature.getMethod().getAnnotation(AuthPermission.class);
        long id = ThreadLocalAuth.get();
        int level = authPermission.level();
        String s = stringRedisTemplate.opsForValue().get(Constant.userPrix + id);
        if(s==null||s.equals("")){
            throw new UnauthorizationException("网站更新，请重新登录");
        }
        List<Authority> authorities = JSONUtil.toList(s, Authority.class);
        int flag = 0;
        for(Authority authority:authorities){
            if(authority.getLevel()<=level){
                flag++;
            }
        }
        if(flag==0){
            throw new UnauthorizationException("权限不足");
        }

    }

}
