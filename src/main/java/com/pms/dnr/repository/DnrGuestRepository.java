/**
 * 
 */
package com.pms.dnr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.dnr.entity.DnrGuest;

/**
 * 
 */
public interface DnrGuestRepository extends JpaRepository<DnrGuest, Long> { 
	
	Optional<DnrGuest> findByPersonalDetailsIdAndDnrStatusTrueAndIsDeletedFalse(Long personalDetailsId);
	
	List<DnrGuest> findByHotelIdAndIsDeletedFalseOrderByCreatedOnDesc(Long hotelId);

}
