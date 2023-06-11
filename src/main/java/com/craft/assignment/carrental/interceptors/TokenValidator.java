package com.craft.assignment.carrental.interceptors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class TokenValidator {

    private final SecretKey jwtSecretKey;

    public TokenValidator(SecretKey jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public boolean validateToken(HttpServletRequest request) {
        String token = extractToken(request);

        if (token != null) {
            try {
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
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

