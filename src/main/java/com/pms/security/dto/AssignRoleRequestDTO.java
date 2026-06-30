/**
 * 
 */
package com.pms.security.dto;

/**
 * 
 */
import lombok.Data;

@Data
public class AssignRoleRequestDTO {

    private Long userId;

    private Long roleId;
}
