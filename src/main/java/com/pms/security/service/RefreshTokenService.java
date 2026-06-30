/**
 * 
 */
package com.pms.security.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.exception.ResourceNotFoundException;
import com.pms.security.entity.RefreshToken;
import com.pms.security.entity.User;
import com.pms.security.repository.RefreshTokenRepository;

/**
 * 
 */

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository repository;

    public RefreshToken createToken(User user) {
        RefreshToken token = new RefreshToken();
        
        
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        
        return repository.save(token);
    }

    public RefreshToken validateToken(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(refreshToken);
            throw new ResourceNotFoundException("Token expired");
        }

        return refreshToken;
    }
}