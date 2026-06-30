/**
 * 
 */
package com.pms.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.security.dto.RolePermissionRequest;
import com.pms.security.entity.RolePermission;
import com.pms.security.service.RolePermissionService;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@RestController
@RequestMapping("/api/role-permissions")
@RequiredArgsConstructor
public class RolePermissionController {

	@Autowired
    private RolePermissionService rolePermissionService;
	
    public RolePermissionController(RolePermissionService rolePermissionService) {
		super();
		this.rolePermissionService = rolePermissionService;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ADMIN','ROLE_HOTEL_OWNER')")
    public ResponseEntity<RolePermission> assignPermission(@RequestBody RolePermissionRequest request) {
        return ResponseEntity.ok(rolePermissionService.assignPermission(request));
    }

	@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ADMIN','ROLE_HOTEL_OWNER')")
    @GetMapping("/{roleId}")
    public ResponseEntity<List<RolePermission>> getPermissionsByRole(@PathVariable Long roleId) {
        return ResponseEntity.ok(rolePermissionService.getPermissionsByRole(roleId));
    }

	@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ADMIN','ROLE_HOTEL_OWNER')")
    @DeleteMapping
    public ResponseEntity<String> removePermission(@RequestParam Long roleId,
                                                   @RequestParam Long permissionId) {
        rolePermissionService.removePermission(roleId, permissionId);
        return ResponseEntity.ok("Permission removed successfully");
    }
}