/**
 * 
 */
package com.pms.security.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.security.dto.UserPermissionRequest;
import com.pms.security.service.IUserPermissionService;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@RestController
//@RequestMapping("/admin/user-permissions")
@RequiredArgsConstructor
public class UserPermissionController {

	private final IUserPermissionService service;

	
	@PostMapping("/admin/user-permissions/assign-permission")
	public ResponseEntity<String> assignPermission(@RequestBody UserPermissionRequest request) {

		service.assignPermission(request);

		return ResponseEntity.ok("Permission Assigned");
	}

	
	@DeleteMapping("/admin/user-permissions/remove-permission")
	public ResponseEntity<String> removePermission(@RequestParam Long userId, @RequestParam Long permissionId) {

		service.removePermission(userId, permissionId);

		return ResponseEntity.ok("Permission Removed");
	}

	@GetMapping("/admin/user-permissions/effective")
	public ResponseEntity<Set<String>> getEffectivePermissions(@RequestParam String username) {

		return ResponseEntity.ok(service.getEffectivePermissions(username));
	}
}
