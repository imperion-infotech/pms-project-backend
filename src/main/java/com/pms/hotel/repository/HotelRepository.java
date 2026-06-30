/**
 * 
 */
package com.pms.hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.hotel.entity.Hotel;

/**
 * 
 */
public interface HotelRepository extends SoftDeleteRepository<Hotel, Long> ,  JpaSpecificationExecutor<Hotel>{
	
	@Modifying
	@Query("""
	UPDATE Hotel h
	SET h.isDeleted = true,
		h.isActive=false,
	    h.deletedOn = CURRENT_TIMESTAMP,
	    h.deletedBy = :deletedBy
	WHERE h.id = :hotelId
	""")
	void softDeleteHotel(Long hotelId, Long deletedBy);
	List<Hotel> findByIsDeletedFalse();
	
//	List<Hotel> findByIdAndIsDeletedFalse(Long hotelId);
	
	Optional<Hotel> findByIdAndIsDeletedFalse(Long hotelId);
}
