/**
 * 
 */
package com.pms.security.service;

import java.util.Set;

import com.pms.security.dto.UserPermissionRequest;

/**
 * 
 */
public interface IUserPermissionService {
	
	 void assignPermission(UserPermissionRequest request);

	    void removePermission(Long userId,
	                          Long permissionId);

	    Set<String> getEffectivePermissions(
	            String username);

}
