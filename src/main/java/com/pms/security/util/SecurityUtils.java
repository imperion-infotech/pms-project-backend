package com.pms.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pms.security.service.AuthService;
import com.pms.util.ConstantUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityUtils {
	
	 @Autowired
	  private static AuthService authService;


    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return "SYSTEM";
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserPrincipal userPrincipal) {
            return userPrincipal.getUsername();
        }

        return auth.getName();
    }

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return 0L;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserPrincipal userPrincipal) {
            return userPrincipal.getUserId();
        }

        return 0L;
    }
    
    public boolean isSuperAdmin() {
        return authService.getCurrentUser()
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("SUPER_ADMIN"));
    }
}