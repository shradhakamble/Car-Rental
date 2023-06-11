package com.craft.assignment.carrental.services;

import com.craft.assignment.carrental.config.JwtConfig;
import com.craft.assignment.carrental.models.repository.DriverInfoset;
import com.craft.assignment.carrental.repository.DriverRepository;
import com.craft.assignment.carrental.utils.HashUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private final DriverRepository driverRepository;

    private final SecretKey jwtSecretKey;

    private final long jwtExpirationTime;

    @Autowired
    public AuthService(DriverRepository driverRepository, SecretKey jwtSecretKey, JwtConfig jwtConfig) {
        this.driverRepository = driverRepository;
        this.jwtSecretKey = jwtSecretKey;
        this.jwtExpirationTime = jwtConfig.getExpirationTime();
    }

    public String authenticateDriver(String email, String password) {
        Optional<DriverInfoset> driverProfile = driverRepository.getDriverByEmail(email);

        if (driverProfile.isPresent() && HashUtils.verifyPassword(password, driverProfile.get().getPassword())) {
            // Generate JWT token
            Instant now = Instant.now();
            Date expirationDate = Date.from(now.plusMillis(jwtExpirationTime));
            return Jwts.builder()
                .setSubject(driverProfile.get().getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(expirationDate)
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();
        }

        return null;
    }

}

