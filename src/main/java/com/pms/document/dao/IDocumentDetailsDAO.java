/**
 * 
 */
package com.pms.document.dao;

import java.util.List;

import com.pms.document.entity.DocumentDetails;

/**
 * 
 */
public interface IDocumentDetailsDAO {
	
	public List<DocumentDetails> getDocumentDetails();
	public DocumentDetails getDocumentDetail(Long documentDetailId);
	public DocumentDetails createDocumentDetails(DocumentDetails documentDetails);
	public DocumentDetails updateDocumentDetails(Long documentDetailsId, DocumentDetails documentDetails);
	public boolean deleteDocumentDetails(Long documentDetailsId);
}
