/**
 * 
 */
package com.pms.security;

/**
 * 
 */

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("permissionChecker")
public class PermissionChecker {

    public boolean hasPermission(Authentication authentication, String permission) {

        boolean isSuperAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));

        if (isSuperAdmin) {
            return true;
        }

        return authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(permission));
    }
}