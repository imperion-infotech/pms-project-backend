/**
 * 
 */
package com.pms.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.security.entity.Permission;

/**
 * 
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
