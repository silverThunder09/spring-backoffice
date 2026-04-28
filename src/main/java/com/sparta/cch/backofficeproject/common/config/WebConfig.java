package com.sparta.cch.backofficeproject.common.config;

import com.sparta.cch.backofficeproject.common.interceptor.LoginCheckInterceptor;
import com.sparta.cch.backofficeproject.common.interceptor.SuperAdminCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;
    private final SuperAdminCheckInterceptor superAdminCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/api/admins/**", "/api/customers/**", "/api/products/**", "/api/orders/**")
                .excludePathPatterns("/api/admins/signup", "/api/admins/login");

        registry.addInterceptor(superAdminCheckInterceptor)
                .addPathPatterns(
                        "/api/admins",
                        "/api/admins/*",
                        "/api/admins/*/role",
                        "/api/admins/*/status",
                        "/api/admins/*/approve",
                        "/api/admins/*/reject"
                )
                .excludePathPatterns(
                        "/api/admins/signup",
                        "/api/admins/login"
                );
    }
}
