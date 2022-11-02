package com.sjj.taskmanagement.config;

import com.sjj.taskmanagement.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//开启权限注解，且在请求之前进行校验
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private CaptchaFilter captchaFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private JwtLogoutSuccessHandler jwtLogoutSuccessHandler;
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }
    //自定义加密方式
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //白名单
    private static final String[] URL_WHITELIST = {
            "/download",
            "/upload",
            "/text/new",
            "/text/old",
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/configuration/ui",
            "/swagge‌​r-ui.html", //swaager的配置资源
            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico",
            "/sys/user/register",//注册接口
            "/location/map",//定位接口
            "/delay",
            "/sys/register/list",
            "/sys/register/registering",
            "/sys/register/finishall",
            "/sys/register/clear",
            "/sys/register/email",
            "/sys/register/report",
            "/sys/register/dead",
            "/getZipFile",
            "/mqtt",
            "/sys/file/loginbg",
            "/sys/file/homebg",
            "/location/switch",
            "/location/switchState",
            "/location/signOn"
    };
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //解决跨域问题，禁用csrf功能
        http.cors().and().csrf().disable()

                // 登录配置
                .formLogin()//自定义登陆，采用表单的形式
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)

                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)

                // 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()//释放白名单
                .anyRequest().authenticated()

                // 异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 配置自定义的过滤器
                .and()
                .addFilter(jwtAuthenticationFilter())
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)//在用户名验证之前进行验证码过滤

        ;

    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
}
