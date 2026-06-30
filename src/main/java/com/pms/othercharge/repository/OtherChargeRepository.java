/**
 * 
 */
package com.pms.othercharge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.document.entity.DocumentType;
import com.pms.othercharge.entity.OtherCharge;

/**
 * 
 */
public interface OtherChargeRepository extends SoftDeleteRepository<OtherCharge, Long>,JpaSpecificationExecutor<OtherCharge>{

	List<OtherCharge> findByHotelId(Long hotelId);
	
	OtherCharge findByIdAndHotelId(Long otherChargeId,Long hotelId);
	
	List<OtherCharge> findByIsDeletedFalseAndHotelId(Long hotelId);
	List<OtherCharge> findByIsDeletedFalse();
	
	@Query("SELECT o FROM OtherCharge o WHERE o.hotelId = :hotelId")
	List<OtherCharge> findOtherCharges(@Param("hotelId") Long hotelId);
}
