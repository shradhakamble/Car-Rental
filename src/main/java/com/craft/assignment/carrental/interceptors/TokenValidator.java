package com.craft.assignment.carrental.interceptors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;

@Component
public class TokenValidator {

    @Value("${jwt.secretKey}")
    private String jwtSecret;

    private Key secretKey;

    @PostConstruct
    public void init() {
       // byte[] decodedSecretKey = Base64.getDecoder().decode(jwtSecret);
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public boolean validateToken(HttpServletRequest request) {
        String token = extractToken(request);

        if (token != null) {
            try {
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

                // Token validation successful
                return true;
            } catch (Exception e) {
                // Token validation failed
                return false;
            }
        }

        // Token not found in the request
        return false;
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;
    }
}

