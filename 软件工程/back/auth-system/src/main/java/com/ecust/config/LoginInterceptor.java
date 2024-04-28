package com.ecust.config;

import cn.hutool.json.JSONUtil;
import com.ecust.auth.advicer.LoginException;
import com.ecust.auth.advicer.NormalException;
import com.ecust.base.ThreadLocalAuth;
import com.ecust.base.constant.Constant;
import com.ecust.base.entity.Authority;
import com.ecust.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");

        try {
            Claims claims = JwtUtils.parseJWT(token);
            String id = (String)claims.get("id");
            Long uid = Long.parseLong(id);
            ThreadLocalAuth.set(uid);
            return true;
        } catch (Exception ex) {
            throw new LoginException("没有登录");
        }
    }
}
