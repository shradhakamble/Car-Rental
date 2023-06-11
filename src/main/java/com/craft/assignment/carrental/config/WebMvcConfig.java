package com.craft.assignment.carrental.config;

import com.craft.assignment.carrental.interceptors.TokenValidationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class responsible for adding the path configuration for interceptor
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenValidationInterceptor tokenValidationInterceptor;

    public WebMvcConfig(TokenValidationInterceptor tokenValidationInterceptor) {
        this.tokenValidationInterceptor = tokenValidationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidationInterceptor)
            .addPathPatterns("/api/driver/registered/**"); // Specify the API endpoints to be intercepted
    }
}