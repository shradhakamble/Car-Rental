package com.craft.assignment.carrental.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.expirationTime}")
    private long expirationTime; // in milliseconds

    public long getExpirationTime() {
        return expirationTime;
    }
}
