/**
 * 
 */
package com.pms.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.exception.ResourceNotFoundException;
import com.pms.exception.UnauthorizedException;
import com.pms.security.entity.User;
import com.pms.security.repository.UserRepository;
import com.pms.security.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class AuthService {

	@Autowired
    private UserRepository userRepository;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
    private HttpServletRequest request;
    
    public static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
	public User getCurrentUser() {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        String token = header.substring(7);

        String username = jwtUtil.extractUsername(token);
        Long hotelId = jwtUtil.extractHotelId(token);

        logger.info("Current Hotel ID: " + hotelId);

        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    public Long getCurrentHotelId() {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        String token = header.substring(7);

        return jwtUtil.extractHotelId(token);
    }
    
    public String getCurrentRole() {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        String token = header.substring(7);

        return jwtUtil.extractRole(token); // you must implement this
    }
}