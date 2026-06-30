/**
 * 
 */
package com.pms.document.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pms.document.dao.IDocumentTypeDAO;
import com.pms.document.entity.DocumentType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * 
 */
@Transactional
@Repository
public class DocumentTypeDAOImpl implements IDocumentTypeDAO {
	
static final Logger logger = LoggerFactory.getLogger(DocumentTypeDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<DocumentType> getDocumentTypes() {
		String hql = "FROM DocumentType as atcl ORDER BY atcl.id";
		return (List<DocumentType>) entityManager.createQuery(hql).getResultList();
	}

	@Override
	public DocumentType getDocumentType(Long documentTypeId) {
		return entityManager.find(DocumentType.class, documentTypeId);
	}

	@Override
	public DocumentType createDocumentType(DocumentType documentType) {
		entityManager.persist(documentType);
		DocumentType b = getLastInsertedDocumentType();
		return b;
	}

	@Override
	public DocumentType updateDocumentType(Long documentTypeId, DocumentType documentType) {
		DocumentType documentTypeFromDB = getDocumentType(documentTypeId);
		documentTypeFromDB.setCreatedOn(documentType.getCreatedOn());
		documentTypeFromDB.setDocumentTypeCategory(documentType.getDocumentTypeCategory());
		documentTypeFromDB.setDocumentTypeDefault(documentType.getDocumentTypeDefault());
		documentTypeFromDB.setDocumentTypeDescription(documentType.getDocumentTypeDescription());
		documentTypeFromDB.setDocumentTypeName(documentType.getDocumentTypeName());
		documentTypeFromDB.setDocumentTypeShortName(documentType.getDocumentTypeShortName());
		entityManager.flush();
		
		DocumentType updatedDocumentType = getDocumentType(documentTypeId);
		
		return updatedDocumentType;
	}

	@Override
	public boolean deleteDocumentType(Long documentTypeId) {
		DocumentType documentType = getDocumentType(documentTypeId);
		entityManager.remove(documentType);
		
		//we are checking here that whether entityManager contains earlier deleted book or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(documentType);
		if(status){
			return false;
		}
		return true;
	}

	@Override
	public DocumentType findById(Integer id) {
	    return entityManager.find(DocumentType.class, id);
	}
	
	private DocumentType getLastInsertedDocumentType(){
		String hql = "from DocumentType order by id DESC";
		Query query = entityManager.createQuery(hql);
		query.setMaxResults(1);
		DocumentType documentType = (DocumentType)query.getSingleResult();
		return documentType;
	}

}
