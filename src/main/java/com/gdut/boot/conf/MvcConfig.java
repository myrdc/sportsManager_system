package com.gdut.boot.conf;

import com.gdut.boot.handler.audit.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/8/13 11:33
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登陆拦截器
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/api/permission/getByNumber")
                .excludePathPatterns("/api/sso/subLogin")
                .excludePathPatterns("/api/sso/getAuth")
                .excludePathPatterns("/api/file/download")
                .excludePathPatterns("/api/sso/logout");
    }
}
