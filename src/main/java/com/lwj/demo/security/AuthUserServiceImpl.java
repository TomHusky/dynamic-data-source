package com.lwj.demo.security;

import com.lwj.demo.system.entity.Role;
import com.lwj.demo.system.entity.User;
import com.lwj.demo.system.service.RoleService;
import com.lwj.demo.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author : lwj
 * @Date: 2020-09-04 15:16
 * @Description : 用户信息加载服务
 */
@Service
public class AuthUserServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 实现了UserDetailsService接口中的loadUserByUsername方法
     * 执行登录,构建Authentication对象必须的信息,
     * 如果用户不存在，则抛出UsernameNotFoundException异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Role role = roleService.getRoleById(user.getRoleId());
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        return new JwtUser(user, grantedAuthorities);
    }
}
