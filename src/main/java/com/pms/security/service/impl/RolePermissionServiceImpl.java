/**
 * 
 */
package com.pms.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.exception.ResourceNotFoundException;
import com.pms.security.dto.RolePermissionRequest;
import com.pms.security.entity.Permission;
import com.pms.security.entity.Role;
import com.pms.security.entity.RolePermission;
import com.pms.security.repository.PermissionRepository;
import com.pms.security.repository.RolePermissionRepository;
import com.pms.security.repository.RoleRepository;
import com.pms.security.service.RolePermissionCacheService;
import com.pms.security.service.RolePermissionService;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService {

	@Autowired
    private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private RolePermissionRepository rolePermissionRepository;
	
	@Autowired
    private RolePermissionCacheService rolePermissionCacheService; // ← ADD THIS

    @Override
    public RolePermission assignPermission(RolePermissionRequest request) {

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        Permission permission = permissionRepository.findById(request.getPermissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));

        rolePermissionRepository.findByRoleIdAndPermissionId(request.getRoleId(), request.getPermissionId())
                .ifPresent(rp -> {
                    throw new ResourceNotFoundException("Permission already assigned to role");
                });

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
        
        RolePermission rolePermission2 = rolePermissionRepository.save(rolePermission);
        rolePermissionCacheService.reloadCache(); 

        return rolePermission2;
    }

    @Override
    public List<RolePermission> getPermissionsByRole(Long roleId) {
        return rolePermissionRepository.findByRoleId(roleId);
    }

    @Override
    public void removePermission(Long roleId, Long permissionId) {

        RolePermission rp = rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Role permission not found"));

        rolePermissionRepository.delete(rp);
        rolePermissionCacheService.reloadCache();
    }
}