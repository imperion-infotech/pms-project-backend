/**
 * 
 */
package com.pms.taxmaster.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.security.entity.User;
import com.pms.taxmaster.entity.TaxMaster;

/**
 * 
 */
public interface TaxMastersRepository extends SoftDeleteRepository<TaxMaster, Integer> , JpaSpecificationExecutor<TaxMaster>{
	
	List<TaxMaster> findByHotelId(Long hotelId);
		
	TaxMaster findByIdAndHotelId(Integer taxMasterId,Long hotelId);
	List<TaxMaster> findByIsDeletedFalseAndHotelId(Long hotelId);
	List<TaxMaster> findByIsDeletedFalse();
	
	@Query("SELECT t FROM TaxMaster t WHERE t.hotelId = :hotelId and t.isDeleted=:isDeleted and t.isActive=:isActive")
	List<TaxMaster> findTaxMasters(@Param("hotelId") Long hotelId,@Param("isDeleted") Boolean isDeleted, @Param("isActive") Boolean isActive);

	List<TaxMaster> findByHotelIdAndIsActiveTrueAndIsDeletedFalse(Long hotelId);
	
	List<TaxMaster> findByHotelIdAndIsActiveTrue(Long hotelId);
	

}
