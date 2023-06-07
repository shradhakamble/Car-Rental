package com.craft.assignment.carrental.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenValidationInterceptor implements HandlerInterceptor {

    private final TokenValidator tokenValidator;

    public TokenValidationInterceptor(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean validToken = tokenValidator.validateToken(request);
        if (validToken) {
            return true; // Token is valid, allow the request to proceed
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //return 401 unauthorized
            return false; // Token is invalid, deny the request
        }
    }
}
