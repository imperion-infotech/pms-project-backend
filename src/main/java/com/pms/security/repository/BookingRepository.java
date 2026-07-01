/**
 * 
 */
package com.pms.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pms.booking.Booking;
import com.pms.building.entity.Building;
import com.pms.common.repository.SoftDeleteRepository;

/**
 * 
 */
public interface BookingRepository extends SoftDeleteRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

	Optional<Booking> findByIdAndIsActiveTrueAndIsDeletedFalseAndHotelId(Long id,Long hotelId);
	
	List<Booking> findByIsDeletedFalseAndHotelId(Long hotelId);
	
	List<Booking> findByIsDeletedFalse();
	
	List<Booking> findByIsDeletedFalseAndIsActiveTrue();
	
	List<Booking> findByIsDeletedFalseAndIsActiveTrueAndHotelId(Long hotelId);
	
	 Booking findByGuestDetailsId(Long guestDetailsId);
	 
	 
	
	}	
	
