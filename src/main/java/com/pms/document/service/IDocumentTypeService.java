/**
 * 
 */
package com.pms.document.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.pms.document.entity.DocumentDetails;
import com.pms.document.entity.DocumentType;
import com.pms.search.specification.DocumentDetailsSpecification;

/**
 * 
 */
public interface IDocumentTypeService {
	
static final Logger logger = LoggerFactory.getLogger(IDocumentTypeService.class);
	
	public List<DocumentType> getDocumentTypes();
	public DocumentType getDocumentType(Long documentTypeId);
	public DocumentType createDocumentType(DocumentType documentType);
	public DocumentType updateDocumentType(Long documentTypeId,DocumentType documentType);
	public boolean deleteDocumentType(Long documentTypeId);
	public DocumentType findByIdAndHotelId(Long id);
	public List<DocumentType> search(String shortName, String documentTypeName,String documentTypeDescription);
}
