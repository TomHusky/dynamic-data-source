package com.lwj.demo.security;

import com.lwj.demo.common.vo.AjaxResult;
import com.lwj.demo.common.util.JSONUtils;
import com.lwj.demo.exception.ExceptionCode;
import com.lwj.demo.exception.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 401返回 token有误  统一处理 AuthenticationException 异常
 *
 * @author lwj
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (authException instanceof JwtException) {
            out.write(JSONUtils.toString(AjaxResult.fail(((JwtException) authException).getCode(), authException.getMessage())));
        } else if (authException instanceof BadCredentialsException) {
            out.write(JSONUtils.toString(AjaxResult.fail(ExceptionCode.UNAUTHORIZED.getCode(), authException.getMessage())));
        } else {
            out.write(JSONUtils.toString(AjaxResult.fail(ExceptionCode.UNAUTHORIZED.getCode(),
                    ExceptionCode.UNAUTHORIZED.getMsg())));
        }
        out.flush();
        out.close();
    }

}
