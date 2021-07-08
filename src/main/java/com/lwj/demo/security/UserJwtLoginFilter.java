package com.lwj.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwj.demo.exception.BaseException;
import com.lwj.demo.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录验证过滤器
 * <p>
 * 该类继承自UsernamePasswordAuthenticationFilter
 * attemptAuthentication ：接收并解析用户凭证。
 *
 * @author lwj
 */
@Component
public class UserJwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Qualifier("myAuthenticationFailureHandler")
    @Autowired
    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Qualifier("myAuthenticationSuccessHandler")
    @Autowired
    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    public UserJwtLoginFilter() {
        super.setFilterProcessesUrl("/api/login");
    }

    /**
     * 接收并解析用户凭证
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            //从输入流中获取到登录的信息
            LoginUser loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(),
                    loginUser.getPassword()));
        } catch (IOException e) {
            throw new BaseException(ExceptionCode.ARGUMENT.getCode(), "登录字段有误");
        }
    }
}