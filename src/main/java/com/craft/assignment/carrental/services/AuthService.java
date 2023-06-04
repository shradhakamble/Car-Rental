package com.craft.assignment.carrental.services;

import com.craft.assignment.carrental.config.JwtConfig;
import com.craft.assignment.carrental.models.DriverProfile;
import com.craft.assignment.carrental.repository.DriverRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecretKey jwtSecretKey;
    private final long jwtExpirationTime;

    @Autowired
    public AuthService(DriverRepository driverRepository, PasswordEncoder passwordEncoder, SecretKey jwtSecretKey, JwtConfig jwtConfig) {
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecretKey = jwtSecretKey;
        this.jwtExpirationTime = jwtConfig.getExpirationTime();
    }

    public String authenticateDriver(String email, String password) {
        Optional<DriverProfile> driverProfile = driverRepository.getDriverByEmail(email);

        if (driverProfile.isPresent() && passwordEncoder.matches(password, driverProfile.get().getContactNumber())) {
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

    public String hashPassword(String password) {
        // Generate a salt
        String salt = BCrypt.gensalt();
        // Hash the password with the salt
        return BCrypt.hashpw(password, salt);
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }


}

