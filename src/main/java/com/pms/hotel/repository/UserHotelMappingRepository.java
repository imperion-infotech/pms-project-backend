/**
 * 
 */
package com.pms.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.security.entity.UserHotelMapping;

/**
 * 
 */
public interface UserHotelMappingRepository extends JpaRepository<UserHotelMapping, Long>{
	
	List<UserHotelMapping> findByUserId(Long userId);

    boolean existsByUserIdAndHotelId(Long userId, Long hotelId);

}
