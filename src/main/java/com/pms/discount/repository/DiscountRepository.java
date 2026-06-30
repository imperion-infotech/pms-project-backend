/**
 * 
 */
package com.pms.discount.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.discount.entity.Discount;

public interface DiscountRepository extends  SoftDeleteRepository<Discount, Long>,JpaRepository<Discount, Long> {
	
	 List<Discount> findByGuestDetailsId(Long guestId);
	 
	 List<Discount> findByHotelIdAndIsDeletedFalseAndIsActiveTrue(Long hotelId);
	 
	 Optional<Discount> findByIdAndHotelIdAndIsDeletedFalse(Long id,Long hotelId);
	 
	 

}