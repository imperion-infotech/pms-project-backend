/**
 * 
 */
package com.pms.hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.hotel.entity.Hotel;
import com.pms.hotel.entity.PropertyByIdDto;

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
	
	 @Query("""
		        select new com.pms.hotel.entity.PropertyByIdDto(
		            h.id, h.hotelName, h.url, h.address, h.city, h.state,
		            h.country, h.zipCode, h.email, h.contactNumber,
		            h.status, h.timezone,
		            u.username, u.email
		        )
		        from UserHotelMapping m
		        join m.hotel h
		        join m.user u
		        join u.roles r
		        where h.id = :hotelId
		          and r.name = 'ROLE_HOTEL_OWNER'
		        """)
		    Optional<PropertyByIdDto> findPropertyById(@Param("hotelId") Long hotelId);
	
	
}
