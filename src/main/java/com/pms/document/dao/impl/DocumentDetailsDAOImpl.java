/**
 * 
 */
package com.pms.document.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pms.document.dao.DocumentDetailsRepository;
import com.pms.document.dao.IDocumentDetailsDAO;
import com.pms.document.entity.DocumentDetails;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * 
 */
@Transactional
@Repository
public class DocumentDetailsDAOImpl implements IDocumentDetailsDAO {
		
		static final Logger logger = LoggerFactory.getLogger(DocumentDetailsDAOImpl.class);
		
		@PersistenceContext
		private EntityManager entityManager;
		
		@Autowired
		private DocumentDetailsRepository documentDetailsRepository;

		@SuppressWarnings("unchecked")
		public List<DocumentDetails> getDocumentDetails() {
			String hql = "FROM DocumentDetails as atcl ORDER BY atcl.id";
			return (List<DocumentDetails>) entityManager.createQuery(hql).getResultList();
		}
		

		public DocumentDetails getDocumentDetail(Long documentDetailId) {
			return entityManager.find(DocumentDetails.class, documentDetailId);
		}

		public DocumentDetails createDocumentDetails(DocumentDetails documentDetails) {
//			entityManager.persist(documentDetails);
			DocumentDetails b = documentDetailsRepository.save(documentDetails);
			return b;
		}

		
		public DocumentDetails updateDocumentDetails(Long documentDetailsId, DocumentDetails documentDetails) {
			//First We are taking Book detail from database by given book id and 
					// then updating detail with provided book object
			DocumentDetails documentDetailsFromDB = getDocumentDetail(documentDetailsId);
			documentDetailsFromDB.setDocumentNumber(documentDetails.getDocumentNumber());
			documentDetailsFromDB.setDocumentType(documentDetails.getDocumentType());
			documentDetailsFromDB.setPersonalDetails(documentDetails.getPersonalDetails());
			documentDetailsFromDB.setFrontImagePath(documentDetails.getFrontImagePath());
			documentDetailsFromDB.setBackImagePath(documentDetails.getBackImagePath());
			documentDetailsFromDB.setRemark(documentDetails.getRemark());
			documentDetailsFromDB.setValidTill(documentDetails.getValidTill());
					documentDetailsRepository.saveAndFlush(documentDetailsFromDB);
					
					DocumentDetails updatedDocumentDetails = getDocumentDetail(documentDetailsId);
					return updatedDocumentDetails;
		}
		
		@Override
		public boolean deleteDocumentDetails(Long documentDetailsId) {
			DocumentDetails documentDetails = getDocumentDetail(documentDetailsId);
//			entityManager.remove(documentDetails);
			documentDetails.setIsDeleted(true);
			documentDetails.setIsActive(false);
			DocumentDetails b= documentDetailsRepository.saveAndFlush(documentDetails);
			
			boolean isDeleted = false;
			if(b.getId() != 0)
			{
				isDeleted=true;
			} else 
			{
				isDeleted=false;
			}
			return isDeleted;
		}


}
