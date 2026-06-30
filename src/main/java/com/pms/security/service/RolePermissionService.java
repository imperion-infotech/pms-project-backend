/**
 * 
 */
package com.pms.security.service;

import java.util.List;

import com.pms.security.dto.RolePermissionRequest;
import com.pms.security.entity.RolePermission;

/**
 * 
 */
public interface RolePermissionService {
	
	RolePermission assignPermission(RolePermissionRequest request);

    List<RolePermission> getPermissionsByRole(Long roleId);

    void removePermission(Long roleId, Long permissionId);

}
