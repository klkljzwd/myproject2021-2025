package com.ecust.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    public static String SECRET = "qwerasdfdxzvdfajjlkjeiojznvxndjkfaowijeiodl";
    public static String createJWT(String id) {
        // 签名方法 HS256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成Jwt的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 生成秘钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);

        // 设置JWT所存储的信息
        JwtBuilder builder = Jwts.builder().setIssuedAt(now).signWith(signatureAlgorithm, signingKey).setClaims(map);

        //builder.claim("name", "value"); //存储自定义信息


        // 构建JWT并将其序列化为紧凑的URL安全字符串
        return builder.compact();
    }

    public static Claims parseJWT(String jwt) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET)).parseClaimsJws(jwt).getBody();
    }



}

