package com.lwj.demo.security;

import com.lwj.demo.common.vo.AjaxResult;
import com.lwj.demo.common.util.JSONUtils;
import com.lwj.demo.exception.ExceptionCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author : lwj
 * @Date: 2020-09-04 16:15
 * @Description : 权限不足403处理器 统一处理 AccessDeniedException 异常
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse resp,
                       AccessDeniedException e) throws IOException {
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.write(JSONUtils.toString(AjaxResult.fail(ExceptionCode.UNAUTHORIZED.getCode(),
                ExceptionCode.UNAUTHORIZED.getMsg())));
        out.flush();
        out.close();
    }
}
