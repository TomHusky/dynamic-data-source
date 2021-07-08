package com.lwj.demo.system.config;

import com.lwj.demo.common.util.JwtUtil;
import com.lwj.demo.system.entity.DataSourceType;
import com.lwj.demo.system.vo.LoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Author: lwj
 * @Package: com.lwj.demo.dynamic.config
 * @ClassName: DataSourceAspect
 * @CreateDate: 2021/5/28 10:50
 * @Description: 动态切换数据源切面
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class DataSourceAspect {
    @Pointcut("execution (* com.lwj.demo..controller.*.*(..))")
    public void dataPointCut() {

    }

    @Before("dataPointCut()")
    public void before(JoinPoint joinPoint) {
        //获取请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        // 从token中解析用户信息
        String token = request.getHeader(JwtUtil.TOKEN_HEADER);
        LoginInfo loginInfo = LoginInfo.getLoginInfoByToken(token);
        if (loginInfo == null) {
            loginInfo = new LoginInfo();
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MyDataSource myDataSource = null;
        //优先判断方法上的注解
        if (method.isAnnotationPresent(MyDataSource.class)) {
            myDataSource = method.getAnnotation(MyDataSource.class);
        } else if (method.getDeclaringClass().isAnnotationPresent(MyDataSource.class)) {
            //其次判断类上的注解
            myDataSource = method.getDeclaringClass().getAnnotation(MyDataSource.class);
        }
        if (myDataSource != null) {
            DataSourceType dataSourceType = myDataSource.type();
            log.info("this is datasource: " + dataSourceType);
            if (dataSourceType.equals(DataSourceType.TENANT)) {
                loginInfo.setTenantId(myDataSource.value());
            }
        }
        LoginInfoHolder.setTenant(loginInfo);
    }

    @After("dataPointCut()")
    public void after(JoinPoint joinPoint) {
        LoginInfoHolder.clear();
    }
}
