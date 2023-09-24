package com.lucky.ssyx.common.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author lucky
 * @date 2023/9/24
 */

/**
 * 登录拦截器配置类
 */
@Configuration
public class LoginMvcConfigurerAdapter extends WebMvcConfigurationSupport {

    @Autowired
    private UserLoginInterceptor userLoginInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor).addPathPatterns("/api/**").excludePathPatterns("/api/user/weixin/wxLogin/*");
        super.addInterceptors(registry);
    }
}
