/**
 * 
 */
package com.pms.security.util;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pms.security.entity.Role;
import com.pms.security.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.function.Function;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private final long expirationMs = 86400000; // 1 day

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user, Long hotelId) {

        Set<Long> roleIds = user.getRoles().stream()
                .map(role -> role.getId())
                .collect(Collectors.toSet());
        
        Set<Role> roles = user.getRoles();
        
        List<String> roleNames = (roles != null)
                ? roles.stream()
                    .map(Role::getName)
                    .toList()
                : List.of();

        if (roles == null || roles.isEmpty()) {
            System.out.println("❌ ROLES EMPTY FOR USER: " + user.getUsername());
            throw new RuntimeException("User has no roles assigned");
        }
        
        System.out.println("DEBUG ROLES:  IN JWTUTIL" + user.getRoles());
        

        if (roles == null || roles.isEmpty()) {
            throw new RuntimeException("User has no roles assigned");
        }

        String primaryRole = roles.stream()
                .findFirst()
                .map(Role::getName)
                .orElse("ROLE_USER");

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", roles.stream()
                        .map(Role::getName)
                        .toList())
                .claim("primaryRole", primaryRole)
                .claim("userId", user.getId())
                .claim("hotelId", hotelId)
                .claim("roleIds", roleIds)  
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSigningKey())
                .compact();
        
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    public Long extractHotelId(String token) {
        return extractAllClaims(token).get("hotelId", Long.class);
    }

    public List<Long> extractRoleIds(String token) {

        Claims claims = extractAllClaims(token);

        List<?> rawRoleIds = claims.get("roleIds", List.class);

        if (rawRoleIds == null) {
            return Collections.emptyList();
        }

        return rawRoleIds.stream()
                .map(id -> Long.valueOf(id.toString()))
                .collect(Collectors.toList());
    }
    
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }
    
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
    
    
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
