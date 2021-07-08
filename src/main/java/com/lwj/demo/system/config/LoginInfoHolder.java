package com.lwj.demo.system.config;

import com.lwj.demo.system.vo.LoginInfo;

/**
 * @Author: lwj
 * @Package: com.lwj.demo.dynamic.config
 * @ClassName: TenantHolder
 * @CreateDate: 2021/5/28 9:52
 * @Description: 存放用户登录标识信息
 */
public class LoginInfoHolder {

    private static final ThreadLocal<LoginInfo> CONTEXT = new ThreadLocal<>();

    public static void setTenant(LoginInfo loginInfo) {
        CONTEXT.set(loginInfo);
    }

    public static LoginInfo getTenant() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
