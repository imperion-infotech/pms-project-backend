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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.security.dto.UserRequestDTO;
import com.pms.security.dto.UserResponseDTO;
import com.pms.security.entity.User;
import com.pms.security.service.IUserService;

/**
 * 
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_CREATE')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_VIEW')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_UPDATE')")
  @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted");
    }
    
    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_SEARCH')")
    @GetMapping("/search")
    public List<UserResponseDTO> searchUser(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) Boolean enabled)
	{

        return userService.search(userName,enabled);
    }
}
