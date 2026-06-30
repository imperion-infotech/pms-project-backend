/**
 * 
 */
package com.pms.security.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.pms.security.entity.User;
import com.pms.security.enums.UserPermissionType;

/**
 * 
 */
@Component
public class PermissionResolver {

	public Set<String> resolveAuthorities(User user) {

		Set<String> authorities = new HashSet<>();

		// Add Roles
		user.getRoles().forEach(role -> {

			authorities.add(role.getName());

			role.getPermissions().forEach(permission -> authorities.add(permission.getName()));
		});

		// User Specific Permissions
		if (user.getUserPermissions() != null) {

			user.getUserPermissions().forEach(up -> {

				String permissionName = up.getPermission().getName();

				if (up.getPermissionType() == UserPermissionType.ALLOW) {

					authorities.add(permissionName);

				} else if (up.getPermissionType() == UserPermissionType.DENY) {

					authorities.remove(permissionName);
				}
			});
		}

		return authorities;
	}
}