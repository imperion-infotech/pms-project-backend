/**
 * 
 */
package com.pms.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.security.service.RolePermissionCacheService;

/**
 * 
 */
@RestController
@RequestMapping("/admin/cache")
public class CacheController {

    @Autowired
    private RolePermissionCacheService rolePermissionCacheService;

    @PostMapping("/reload")
//    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'RELOAD_CACHE')")
//    @PreAuthorize("hasAuthority('RELOAD_CACHE')")  // only super admin can reload
    public ResponseEntity<String> reloadCache() {
    	
        rolePermissionCacheService.reloadCache();
        return ResponseEntity.ok("Cache reloaded successfully");
    }
}