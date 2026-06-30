/**
 * 
 */
package com.pms.security.controller;

/**
 * 
 */
import com.pms.security.dto.AssignRoleRequestDTO;
import com.pms.security.service.UserRoleService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

//    @PreAuthorize("hasAuthority('USER_ROLE_ASSIGN')")
    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_ROLE_ASSIGN')")
    @PostMapping("/assign")
    public ResponseEntity<String> assignRole(
            @RequestBody AssignRoleRequestDTO dto) {

        userRoleService.assignRole(dto);

        return ResponseEntity.ok("Role assigned successfully");
    }

//    @PreAuthorize("hasAuthority('USER_ROLE_REMOVE')")
    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_ROLE_REMOVE')")
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeRole(
            @RequestBody AssignRoleRequestDTO dto) {

        userRoleService.removeRole(dto);

        return ResponseEntity.ok("Role removed successfully");
    }

    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_ROLE_VIEW')")
//    @PreAuthorize("hasAuthority('USER_ROLE_VIEW')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserRoles(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                userRoleService.getUserRoles(userId));
    }
}