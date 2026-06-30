/**
 * 
 */
package com.pms.security.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.pms.security.dto.UserPermissionRequest;
import com.pms.security.entity.Permission;
import com.pms.security.entity.User;
import com.pms.security.entity.UserPermission;
import com.pms.security.enums.UserPermissionType;
import com.pms.security.repository.PermissionRepository;
import com.pms.security.repository.UserPermissionRepository;
import com.pms.security.repository.UserRepository;
import com.pms.security.service.IUserPermissionService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserPermissionServiceImpl implements IUserPermissionService {

	private final UserRepository userRepository;
	private final PermissionRepository permissionRepository;
	private final UserPermissionRepository userPermissionRepository;

	public void assignPermission(UserPermissionRequest request) {

		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		Permission permission = permissionRepository.findById(request.getPermissionId())
				.orElseThrow(() -> new RuntimeException("Permission not found"));

		Optional<UserPermission> existing = userPermissionRepository.findByUserIdAndPermissionId(user.getId(),
				permission.getId());

		if (existing.isPresent()) {

			existing.get().setPermissionType(request.getPermissionType());

			userPermissionRepository.save(existing.get());

			return;
		}

		UserPermission userPermission = UserPermission.builder().user(user).permission(permission)
				.permissionType(request.getPermissionType()).build();

		userPermissionRepository.save(userPermission);
	}

	@Override
	public void removePermission(Long userId, Long permissionId) {

		UserPermission permission = userPermissionRepository.findByUserIdAndPermissionId(userId, permissionId)
				.orElseThrow(() -> new RuntimeException("Permission not found"));

		userPermissionRepository.delete(permission);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> getEffectivePermissions(String username) {

		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		Set<String> finalPermissions = new HashSet<>();

		// Role permissions
		user.getRoles().forEach(
				role -> role.getPermissions().forEach(permission -> finalPermissions.add(permission.getName())));

		// User ALLOW permissions
		user.getUserPermissions().stream().filter(p -> p.getPermissionType() == UserPermissionType.ALLOW)
				.forEach(p -> finalPermissions.add(p.getPermission().getName()));

		// User DENY permissions
		user.getUserPermissions().stream().filter(p -> p.getPermissionType() == UserPermissionType.DENY)
				.forEach(p -> finalPermissions.remove(p.getPermission().getName()));

		return finalPermissions;
	}
}