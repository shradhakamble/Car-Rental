package com.craft.assignment.carrental.interceptors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class TokenValidator {

   private SecretKey jwtSecretKey;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public boolean validateToken(HttpServletRequest request) {
        String token = extractToken(request);
        String sessionId = request.getHeader("Session-Id");
        String key = secretKey + sessionId;

        this.jwtSecretKey = Keys.hmacShaKeyFor(key.getBytes());

        if (token != null && sessionId != null) {
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

