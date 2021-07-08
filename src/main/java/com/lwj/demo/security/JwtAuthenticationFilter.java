package com.lwj.demo.security;

import com.lwj.demo.common.util.JwtUtil;
import com.lwj.demo.exception.ExceptionCode;
import com.lwj.demo.exception.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token的校验
 * OncePerRequestFilter， 确保在一次请求只通过一次filter，而不需要重复执行，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 *
 * @author lwj on 2017/9/13.
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Qualifier("authUserServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        String tokenHeader = request.getHeader(JwtUtil.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null || StringUtils.isEmpty(tokenHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // 如果不是以指定字符串开头则直接返回失败
            if (!tokenHeader.startsWith(JwtUtil.TOKEN_PREFIX)) {
                throw new JwtException("token格式错误");
            }
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            // token错误直接返回信息
            log.error(e.getMessage(), JwtAuthenticationFilter.class);
            SecurityContextHolder.clearContext();
            this.authenticationEntryPoint.commence(request, response, e);
        }
    }

    /**
     * 这里从token中获取用户信息
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(JwtUtil.TOKEN_PREFIX, JwtUtil.EMPTY_STRING);
        try {
            String username = JwtUtil.getTokenBody(token, JwtUtil.TOKEN_SECRET).getSubject();
            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                }
            }
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage(), JwtAuthenticationFilter.class);
            throw new JwtException(ExceptionCode.TOKEN_EXPIRE.getCode(), ExceptionCode.TOKEN_EXPIRE.getMsg());
        } catch (MalformedJwtException e) {
            log.error(e.getMessage(), JwtAuthenticationFilter.class);
            throw new JwtException(ExceptionCode.TOKEN_ERROR.getCode(), ExceptionCode.TOKEN_ERROR.getMsg());
        } catch (Exception e) {
            throw new JwtException(ExceptionCode.TOKEN_ERROR.getCode(), ExceptionCode.TOKEN_ERROR.getMsg());
        }
        return null;
    }
}