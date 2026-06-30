/**
 * 
 */
package com.pms.document.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pms.auditlog.annotation.Auditable;
import com.pms.common.service.SoftDeleteService;
import com.pms.document.dao.DocumentTypeRepository;
import com.pms.document.dao.IDocumentTypeDAO;
import com.pms.document.entity.DocumentType;
import com.pms.document.service.IDocumentTypeService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.search.specification.DocumentTypeSpecification;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.BaseHotelService;

/**
 * 
 */
@Service
public class DocumentTypeServiceImpl extends BaseHotelService implements IDocumentTypeService {
	
	@Autowired
	private IDocumentTypeDAO dao;
	
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	
	@Autowired
	private SoftDeleteService softDeleteService;
	
	public DocumentTypeServiceImpl(IDocumentTypeDAO dao, DocumentTypeRepository documentTypeRepository) {
		super();
		this.dao = dao;
		this.documentTypeRepository = documentTypeRepository;
	}

	@Override
	public List<DocumentType> getDocumentTypes() {
		Long hotelId = HotelContext.getHotelId();

	    if (hotelId == null) {
	        throw new ResourceNotFoundException("Hotel not selected");
	    }
	    validateHotelAccess(hotelId);
	    if (isSuperAdmin()) 
	    	return documentTypeRepository.findByIsDeletedFalse();
	    else 
		return documentTypeRepository.findByIsDeletedFalseAndHotelId(HotelContext.getHotelId());
	}

	@Override
	public DocumentType getDocumentType(Long documentTypeId) {
		Long hotelId = HotelContext.getHotelId();

	    if (hotelId == null) {
	        throw new ResourceNotFoundException("Hotel not selected");
	    }
		validateHotelAccess(hotelId);
		return documentTypeRepository.findByIdAndHotelId(documentTypeId,HotelContext.getHotelId());
	}

	@Override
	@Auditable(action = "CREATE", entity = "DOCUMENTTYPE")
	public DocumentType createDocumentType(DocumentType documentType) {
		Long userId = UserContext.getUserId();

	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
	    assignHotel(documentType, documentType.getHotelId());
	    documentType.setCreatedBy(userId);
		 return documentTypeRepository.save(documentType);
	}

	@Override
	@Auditable(action = "UPDATE", entity = "DOCUMENTTYPE")
	public DocumentType updateDocumentType(Long documentTypeId, DocumentType documentType) {
		
		validateHotelAccess(documentType.getHotelId());
		Long userId = UserContext.getUserId();
	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
	    documentType.setUpdatedBy(userId);
	    documentType.setUpdatedOn(LocalDateTime.now());
		return dao.updateDocumentType(documentTypeId, documentType);
	}

	@Override
	@Auditable(action = "DELETE", entity = "DOCUMENTTYPE")
	public boolean deleteDocumentType(Long documentTypeId) {
		DocumentType b = getDocumentType(documentTypeId);
		validateHotelAccess(b.getHotelId());
		DocumentType c = softDeleteService.softDelete(documentTypeId, documentTypeRepository);
		return c == null ? false : true;
	}

	@Override
	public DocumentType findByIdAndHotelId(Long id) {
		  return documentTypeRepository.findByIdAndHotelId(id,HotelContext.getHotelId());
	}
	
	
	@Override
	public 	List<DocumentType> search(String shortName, String documentTypeName,String documentTypeDescription)
	{   
		Long hotelId = HotelContext.getHotelId();   // 🔥 get from JWT

	     if (hotelId == null) {
	         throw new ResourceNotFoundException("Hotel not selected");
	     }
	     validateHotelAccess(hotelId);
	Specification<DocumentType> spec = Specification
					.where(DocumentTypeSpecification.hasHotelId(hotelId))
	                .and(DocumentTypeSpecification.hasShortName(shortName))
	                .and(DocumentTypeSpecification.hasDocumentTypeName(documentTypeName))
	                .and(DocumentTypeSpecification.hasDocumentTypeDescription(documentTypeDescription));

	        return documentTypeRepository.findAll(spec);
	    }


}
