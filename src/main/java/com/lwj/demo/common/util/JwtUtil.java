package com.lwj.demo.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {

    private JwtUtil() {

    }

    /**
     * 过期时间 10 小时
     */
    private static final long EXPIRE_TIME = 10 * 60 * 60 * 1000L;

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String EMPTY_STRING = "";

    /**
     * token秘钥
     */
    public static final String TOKEN_SECRET = "greenbon_91440101MA5CCBE365";

    /**
     * 附带额外信息
     */
    public static final String EXTEND_INFO = "extendInfo";

    /**
     * JWT签发者
     */
    private static final String ISS = "greenbon";


    /**
     * 生成签名
     *
     * @param username   用户名
     * @param secret     加盐
     * @param extendInfo 额外参数
     * @return java.lang.String
     */
    public static String createToken(String username, String secret, Object extendInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put(EXTEND_INFO, extendInfo);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secret)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .compact();
    }

    /**
     * 生成签名
     *
     * @param username   用户名
     * @param secret     加盐
     * @param extendInfo 额外参数
     * @param extendInfo 过期时间
     * @return java.lang.String
     */
    public static String createToken(String username, String secret, Object extendInfo, Long expireTime) {
        Map<String, Object> map = new HashMap<>();
        map.put(EXTEND_INFO, extendInfo);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secret)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .compact();
    }

    /**
     * 生成签名
     *
     * @param username 用户名
     * @param secret   秘钥
     * @return java.lang.String
     */
    public static String createToken(String username, String secret) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .compact();
    }

    /**
     * 解析jwt
     */
    public static Claims getTokenBody(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
