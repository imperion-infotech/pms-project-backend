/**
 * 
 */
package com.pms.security.service;

/**
 * 
 */
import java.util.List;

import com.pms.security.dto.AssignRoleRequestDTO;

public interface UserRoleService {

    void assignRole(AssignRoleRequestDTO dto);

    void removeRole(AssignRoleRequestDTO dto);

    List<String> getUserRoles(Long userId);
}