package com.lwj.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author lwj
 * spring-security权限管理的核心配置
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("authUserServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 权限过滤器（当前url所需要的访问权限）
     */
    @Autowired
    private MyFilterInvocationSecurityMetadataSource filterMetadataSource;

    /**
     * 拦截器
     */
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 权限决策器
     */
    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;

    /**
     * 自定义错误(401)返回数据
     */
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * 权限不足403处理器
     */
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;


    @Autowired
    private UserJwtLoginFilter userJwtLoginFilter;

    /**
     * 装载BCrypt密码编码器
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置userDetails的数据源，密码加密格式
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    /**
     * 配置不拦截的路径
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/doc.html")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/resources/**")
                .antMatchers("/webjars/**")
                .antMatchers("/swagger-resources/**");
    }

    /**
     * HttpSecurity包含了原数据（主要是url）
     * 通过withObjectPostProcessor将MyFilterInvocationSecurityMetadataSource和MyAccessDecisionManager注入进来
     * 此url先被MyFilterInvocationSecurityMetadataSource处理，然后 丢给 MyAccessDecisionManager处理
     * 如果不匹配，返回 MyAccessDeniedHandler
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                .logout().disable()
                // 使用 JWT，关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 所有请求必须认证
                .and()
                .authorizeRequests()
                .anyRequest()
                // RBAC 动态 url 认证
                .access("@rbacauthorityservice.hasPermission(request,authentication)");
        // 无权访问 JSON 格式的数据
        httpSecurity.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        httpSecurity.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
        httpSecurity.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                o.setSecurityMetadataSource(filterMetadataSource);
                o.setAccessDecisionManager(myAccessDecisionManager);
                return o;
            }

        });
        //用重写的Filter替换掉原有的UsernamePasswordAuthenticationFilter实现使用json 数据也可以登陆
        httpSecurity.addFilterAt(userJwtLoginFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
