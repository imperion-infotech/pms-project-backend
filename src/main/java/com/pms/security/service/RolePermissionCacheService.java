/**
 * 
 */
package com.pms.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.pms.security.configuration.SecurityConfig;
import com.pms.security.entity.Role;
import com.pms.security.repository.PermissionRepository;
import com.pms.security.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class RolePermissionCacheService {
	
	static final Logger logger = LoggerFactory.getLogger(RolePermissionCacheService.class);

	@Autowired
    private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;

    private final Map<Long, List<GrantedAuthority>> rolePermissionCache = new ConcurrentHashMap<>();
    
    // ✅ ADD THIS — load cache on startup
    @PostConstruct
    public void initCache() {
        reloadCache();
    }
    
 // ✅ ADD THIS — reload all roles from DB into cache
    public void reloadCache() {
        logger.info("Reloading role-permission cache...");
        rolePermissionCache.clear();
        List<Role> allRoles = roleRepository.findAll();
        for (Role role : allRoles) {
            rolePermissionCache.put(role.getId(), loadAuthoritiesFromDb(role.getId()));
        }
        logger.info("Cache reloaded with {} roles", rolePermissionCache.size());
    }

    public List<GrantedAuthority> getAuthoritiesByRoleIds(List<Long> roleIds) {

        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Long roleId : roleIds) {

        	// ❌ Current code - computeIfAbsent loads from DB directly
//        	List<GrantedAuthority> cachedAuthorities =
//        	        rolePermissionCache.computeIfAbsent(roleId, this::loadAuthoritiesFromDb);

        	// ✅ Fix - use reloadCache() result, don't bypass cache
        	List<GrantedAuthority> cachedAuthorities = rolePermissionCache.get(roleId);
        	if (cachedAuthorities == null) {
        	    cachedAuthorities = loadAuthoritiesFromDb(roleId);
        	    rolePermissionCache.put(roleId, cachedAuthorities);
        	}

            authorities.addAll(cachedAuthorities);
        }

        return authorities;
    }
    
    public Collection<? extends GrantedAuthority> getAllAuthorities() {
        return permissionRepository.findAll()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());
    }

    private List<GrantedAuthority> loadAuthoritiesFromDb(Long roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Set<String> permissions = new HashSet<>();

        permissions.add(role.getName());

        role.getPermissions().forEach(permission ->
                permissions.add(permission.getName())
        );

        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void evictRole(Long roleId) {
        rolePermissionCache.remove(roleId);
    }

    public void clearAll() {
        rolePermissionCache.clear();
    }
}
