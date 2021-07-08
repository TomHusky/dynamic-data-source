package com.lwj.demo.security;

import com.lwj.demo.common.util.JwtUtil;
import com.lwj.demo.common.vo.AjaxResult;
import com.lwj.demo.common.util.JSONUtils;
import com.lwj.demo.system.entity.User;
import com.lwj.demo.system.vo.LoginInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : lwj
 * @Date: 2020-09-04 15:43
 * @Description : 登录认证成功的处理
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication auth) throws IOException {
        JwtUser jwtUser = (JwtUser) auth.getPrincipal();
        User user = jwtUser.getUser();
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserId(user.getId());
        loginInfo.setTenantId(user.getTenantId());
        String token = JwtUtil.createToken(jwtUser.getUsername(), JwtUtil.TOKEN_SECRET, loginInfo);
        Map<String, Object> map = new HashMap<>(7);
        map.put("username", user.getUsername());
        map.put("nickname", user.getNickname());
        map.put("type", user.getType());
        map.put("id", user.getId());
        map.put("avatar", user.getAvatar());
        map.put(JwtUtil.TOKEN_HEADER, JwtUtil.TOKEN_PREFIX + token);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONUtils.toString(AjaxResult.success(map)));
    }
}