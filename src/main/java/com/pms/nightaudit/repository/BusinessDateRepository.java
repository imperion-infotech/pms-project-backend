package com.pms.nightaudit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.nightaudit.entity.BusinessDate;

public interface BusinessDateRepository extends JpaRepository<BusinessDate, Long> {

	Optional<BusinessDate> findByHotelIdAndIsActiveTrue(Long hotelId);
	
	 Optional<BusinessDate> findByHotelId(Long hotelId);
	 
	 Optional<BusinessDate> findTopByHotelIdAndIsActiveTrueAndIsDeletedFalse(Long hotelId);
	 
	    boolean existsByHotelId(Long hotelId);
	    
	
	BusinessDate findTopByHotelIdOrderByIdDesc(Long hotelId);
	
	Optional<BusinessDate>
	findByHotelIdAndCurrentBusinessDateTrue(Long hotelId);
	
	Optional<BusinessDate>
	findByHotelIdAndCurrentBusinessDateTrueAndIsActiveTrueAndIsDeletedFalse(Long hotelId);
}