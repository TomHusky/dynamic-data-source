package com.lwj.demo.security;

import com.lwj.demo.common.vo.AjaxResult;
import com.lwj.demo.common.util.JSONUtils;
import com.lwj.demo.exception.JwtException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author : lwj
 * @Date: 2020-09-04 15:38
 * @Description : 登录认证验证失败的处理
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        AjaxResult<String> ajaxResult;
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            ajaxResult = AjaxResult.fail("账户名或者密码输入错误!");
        } else if (exception instanceof LockedException) {
            ajaxResult = AjaxResult.fail("账户被锁定，请联系管理员!");
        } else if (exception instanceof CredentialsExpiredException) {
            ajaxResult = AjaxResult.fail("密码过期，请联系管理员!");
        } else if (exception instanceof AccountExpiredException) {
            ajaxResult = AjaxResult.fail("账户过期，请联系管理员!");
        } else if (exception instanceof DisabledException) {
            ajaxResult = AjaxResult.fail("账户被禁用，请联系管理员!");
        } else if (exception instanceof JwtException) {
            ajaxResult = AjaxResult.fail(exception.getMessage());
        } else {
            ajaxResult = AjaxResult.fail("登录失败!");
        }
        response.getWriter().write(JSONUtils.toString(ajaxResult));
    }
}