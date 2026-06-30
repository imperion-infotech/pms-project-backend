/**
 * 
 */
package com.pms.security.service.impl;

/**
 * 
 */
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.security.dto.AssignRoleRequestDTO;
import com.pms.security.entity.Role;
import com.pms.security.entity.User;
import com.pms.security.repository.RoleRepository;
import com.pms.security.repository.UserRepository;
import com.pms.security.service.UserRoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	@Override
	public void assignRole(AssignRoleRequestDTO dto) {

		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

		Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));

		user.getRoles().add(role);

		userRepository.save(user);
	}

	@Override
	public void removeRole(AssignRoleRequestDTO dto) {

		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

		user.getRoles().removeIf(role -> role.getId().equals(dto.getRoleId()));

		userRepository.save(user);
	}

	@Override
	public List<String> getUserRoles(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		return user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
	}
}