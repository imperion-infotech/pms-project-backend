/**
 * 
 */
package com.pms.document.service;

import java.util.List;

import com.pms.document.entity.DocumentDetails;

/**
 * 
 */
public interface IDocumentDetailsService {
	
	public List<DocumentDetails> getDocumentDetails();
	public <Optional>DocumentDetails getDocumentDetail(Long documentDetailId);
	public DocumentDetails createDocumentDetails(DocumentDetails documentDetails);
	public DocumentDetails updateDocumentDetails(Long documentDetailsId, DocumentDetails documentDetails);
	public boolean deleteDocumentDetails(Long documentDetailsId);
	 public List<DocumentDetails> search(String documentTypeEnum, String documentNumber);

}
