/**
 * 
 */
package com.pms.security.dto;

import com.pms.security.enums.UserPermissionType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class UserPermissionRequest {

    private Long userId;

    private Long permissionId;

    private UserPermissionType permissionType;
}