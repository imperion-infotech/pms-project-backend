/**
 * 
 */
package com.pms.rent.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pms.booking.Booking;
import com.pms.common.repository.SoftDeleteRepository;
import com.pms.rent.RentDetails;

/**
 * 
 */
public interface RentDetailsRepository extends SoftDeleteRepository<RentDetails, Long>, JpaSpecificationExecutor<RentDetails>{
	
	RentDetails findByIdAndHotelIdAndIsDeletedFalse(Long id, Long hotelId);
	List<RentDetails> findByHotelIdAndIsDeletedFalse(Long hotelId);
	List<RentDetails> findByIsDeletedFalse();
	Optional<RentDetails> findByIdAndIsDeletedFalse(Long id);
	Optional<RentDetails> findByPersonalDetailsId(Long personalDetailsId);

}
