/**
 * 
 */
package com.pms.document.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.document.entity.DocumentType;

/**
 * 
 */
public interface IDocumentTypeDAO {
	
static final Logger logger = LoggerFactory.getLogger(IDocumentTypeDAO.class);
	
	public List<DocumentType> getDocumentTypes();
	public DocumentType getDocumentType(Long documentTypeId);
	public DocumentType createDocumentType(DocumentType documentType);
	public DocumentType updateDocumentType(Long documentTypeId,DocumentType documentType);
	public boolean deleteDocumentType(Long documentTypeId);
	public DocumentType findById(Integer id);


}
