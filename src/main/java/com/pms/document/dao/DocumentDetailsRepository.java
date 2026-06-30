/**
 * 
 */
package com.pms.document.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.document.entity.DocumentDetails;
import com.pms.personaldetails.PersonalDetails;

/**
 * 
 */
public interface DocumentDetailsRepository extends SoftDeleteRepository<DocumentDetails, Long> , JpaSpecificationExecutor<DocumentDetails>{
	
	Optional<DocumentDetails> findByIdAndHotelIdAndIsDeletedFalse(Long id, Long hotelId);
	List<DocumentDetails> findByHotelIdAndIsDeletedFalse(Long hotelId);
	
	List<DocumentDetails> findByIsDeletedFalseAndHotelId(Long hotelId);
	List<DocumentDetails> findByIsDeletedFalse();
	
	
}
