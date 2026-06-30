/**
 * 
 */
package com.pms.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.security.entity.UserPermission;

/**
 * 
 */
@Repository
public interface UserPermissionRepository
        extends JpaRepository<UserPermission, Long> {

    List<UserPermission> findByUserIdAndHotelIdAndIsDeletedFalse(
            Long userId,
            Long hotelId);

    boolean existsByUserIdAndPermissionId(
            Long userId,
            Long permissionId);

    Optional<UserPermission> findByUserIdAndPermissionId(
            Long userId,
            Long permissionId);
}