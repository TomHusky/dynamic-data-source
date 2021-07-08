package com.lwj.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @Author : lwj
 * @Date: 2020-09-04 15:35
 * @Description : 自定义权限决策管理器 知道了当前访问的url需要的具体权限，
 * 接下来就是决策当前的访问是否能通过权限验证了
 */
@Component
@Slf4j
public class MyAccessDecisionManager implements AccessDecisionManager {
    /**
     * 取当前用户的权限与这次请求的这个url需要的权限作对比，决定是否放行
     * auth 包含了当前的用户信息，包括拥有的权限,即之前UserDetailsService登录时候存储的用户对象
     * object 就是FilterInvocation对象，可以得到request等web资源。
     * cas 是本次访问需要的权限。即上一步的 MyFilterInvocationSecurityMetadataSource 中查询核对得到的权限列表
     **/
    @Override
    public void decide(Authentication auth, Object object, Collection<ConfigAttribute> cas) {
//        for (ConfigAttribute configAttribute : cas) {
//            if (auth == null) {
//                throw new AccessDeniedException("权限不足");
//            }
//            //当前请求需要的权限
//            String needRole = configAttribute.getAttribute();
//            if ("ROLE_LOGIN".equals(needRole) && auth instanceof AnonymousAuthenticationToken) {
//                throw new BadCredentialsException("未登录");
//            }
//            //当前用户所具有的权限
//            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
//            for (GrantedAuthority authority : authorities) {
//                if (authority.getAuthority().equals(needRole)) {
//                    return;
//                }
//            }
//        }
//        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
