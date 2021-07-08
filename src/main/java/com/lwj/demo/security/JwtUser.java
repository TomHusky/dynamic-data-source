package com.lwj.demo.security;

import com.lwj.demo.common.enums.UserStatus;
import com.lwj.demo.system.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author lwj
 */
public class JwtUser implements UserDetails {
    private static final long serialVersionUID = -4007399541170464616L;
    private final User user;
    /**
     * 用户包含的角色列表
     */
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUser(User user) {
        this.user = user;
    }

    public JwtUser(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public User getUser() {
        return user;
    }

    /**
     * 返回分配给用户的角色列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    /**
     * 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserStatus.LOCK.getValue() != user.getStatus();
    }

    /**
     * 密码是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否激活
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}