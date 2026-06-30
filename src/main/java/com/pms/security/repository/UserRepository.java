/**
 * 
 */
package com.pms.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pms.security.dto.UserResponseDTO;
import com.pms.security.entity.User;

/**
 * 
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User>{
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    Optional<User> findByEmail(String email);
    List<User> findByIsDeletedFalse();
    Optional<User> findByIdAndIsDeletedFalse(Long id);
    
    /*@Query("""
    	    SELECT DISTINCT u
    	    FROM User u
    	    LEFT JOIN FETCH u.roles r
    	    LEFT JOIN FETCH r.permissions
    	    WHERE u.username = :username
    	    AND u.isDeleted = false
    	""")
    	User findByUsernameWithRolesAndPermissions(@Param("username") String username);*/
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username and  u.isDeleted = false")
    Optional<User> findByUsernameWithRolesAndIsDeletedFalse(@Param("username") String username);
    
    @Query("""
    	    SELECT u FROM User u
    	    LEFT JOIN FETCH u.roles
    	    LEFT JOIN FETCH u.mappings m
    	    LEFT JOIN FETCH m.hotel
    	    WHERE u.username = :username
    	""")
    	Optional<User> findFullUser(@Param("username") String username);
    
    List<User> findByIsDeletedFalseAndHotelId(Long hotelId);
    
    boolean existsByUsernameAndIdNot(String username, Long id);
    
    boolean existsByUsername(String username);
    
    @Query("""
    	    SELECT COUNT(u) > 0
    	    FROM User u
    	    JOIN u.roles r
    	    WHERE r.name = :roleName and u.enabled=true and u.isDeleted=false
    	""")
    	boolean existsByRoles_NameAndEnabledTrue(@Param("roleName") String roleName);
    
    Optional<User> findByUsername(String username);
    
    @Query("""
    	       SELECT DISTINCT u
    	       FROM User u
    	       LEFT JOIN FETCH u.roles r
    	       LEFT JOIN FETCH r.permissions
    	       LEFT JOIN FETCH u.userPermissions up
    	       LEFT JOIN FETCH up.permission
    	       WHERE u.username = :username
    	       AND u.isDeleted = false
    	       """)
    	Optional<User> findByUsernameWithRolesAndPermissions(@Param("username") String username);
    
}

