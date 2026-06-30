/**
 * 
 */
package com.pms.document.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.building.entity.Building;
import com.pms.common.repository.SoftDeleteRepository;
import com.pms.document.entity.DocumentType;

/**
 * 
 */
public interface DocumentTypeRepository
		extends SoftDeleteRepository<DocumentType, Long>, JpaSpecificationExecutor<DocumentType> {

	List<DocumentType> findByHotelId(Long hotelId);

	DocumentType findByIdAndHotelId(Long documentTypeId, Long hotelId);
	
	List<DocumentType> findByIsDeletedFalseAndHotelId(Long hotelId);
	List<DocumentType> findByIsDeletedFalse();

	@Query("SELECT d FROM DocumentType d WHERE d.hotelId = :hotelId and d.isDeleted=:isDeleted and d.isActive=:isActive")
	List<DocumentType> findDocumentTypes(@Param("hotelId") Long hotelId, @Param("isDeleted") Boolean isDeleted,
			@Param("isActive") Boolean isActive);

}
