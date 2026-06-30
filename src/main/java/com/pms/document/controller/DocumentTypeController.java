/**
 * 
 */
package com.pms.document.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.auditlog.util.AuditUtil;
import com.pms.document.entity.DocumentType;
import com.pms.document.service.IDocumentTypeService;

import jakarta.servlet.http.HttpSession;

/**
 * 
 */
@RestController
public class DocumentTypeController {
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentTypeController.class);

	@Autowired
	private IDocumentTypeService service;
	
//	@PreAuthorize("hasAuthority('DOCUMENTTYPE_VIEW')")
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTTYPE_VIEW')")
	@GetMapping("/user/getdocumenttypes")
	public ResponseEntity<List<DocumentType>> getDocumentTypes() {
		List<DocumentType> documentTypes = service.getDocumentTypes();
		return new ResponseEntity<List<DocumentType>>(documentTypes, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAuthority('DOCUMENTTYPE_VIEW')")
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTTYPE_VIEW')")
	@GetMapping("/user/getdocumenttype/{id}")
	public ResponseEntity<DocumentType> getDocumentType(@PathVariable("id") Long id) {
		DocumentType documentType = service.getDocumentType(id);
		return new ResponseEntity<DocumentType>(documentType, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAuthority('DOCUMENTTYPE_CREATE')")
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTTYPE_CREATE')")
	@PostMapping("/admin/createdocumenttype")
	public ResponseEntity<?> createDocumentType(@RequestBody DocumentType documentType) {
		// Validate input
		if (documentType == null || documentType.getDocumentTypeCategory() == null || documentType.getDocumentTypeCategory().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("DocumentType category must not be null or empty");
		}
		
		if (documentType == null || documentType.getDocumentTypeName() == null || documentType.getDocumentTypeName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("DocumentType name must not be null or empty");
		}
		
		if (documentType == null || documentType.getDocumentTypeDescription() == null || documentType.getDocumentTypeDescription().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("DocumentType description must not be null or empty");
		}

		if (documentType == null || documentType.getDocumentTypeShortName() == null || documentType.getDocumentTypeShortName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("DocumentType documentTypeShortName must not be null or empty");
		}
		
		try {
			DocumentType savedDocumentType = service.createDocumentType(documentType);
			return ResponseEntity.ok(savedDocumentType);
		} catch (Exception e) {
			logger.error("Exception in controller of createdocumenttype api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the document type");
		}
	}
	
//	@PreAuthorize("hasAuthority('DOCUMENTTYPE_UPDATE')")
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTTYPE_UPDATE')")
	@PutMapping("/admin/updatedocumenttype/{id}")
	public ResponseEntity<?> updateDocumentType(@PathVariable Long id, @RequestBody DocumentType documentType,HttpSession session) {
		// Validate input
		if (documentType == null || documentType.getDocumentTypeName() == null || documentType.getDocumentTypeName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("documentType name must not be null or empty");
		}

		try {
			// Find existing floor
			DocumentType existingDocumentType = service.getDocumentType(id);
			if (existingDocumentType == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DocumentType with ID " + id + " not found");
			}
			session.setAttribute("oldValue", AuditUtil.toJson(existingDocumentType));
			// Update fields
			existingDocumentType.setDocumentTypeCategory(documentType.getDocumentTypeCategory());
			existingDocumentType.setDocumentTypeDefault(documentType.getDocumentTypeDefault());
			existingDocumentType.setDocumentTypeDescription(documentType.getDocumentTypeDescription());
			existingDocumentType.setDocumentTypeName(documentType.getDocumentTypeName());
			existingDocumentType.setDocumentTypeShortName(documentType.getDocumentTypeShortName());
			

			// You can add more setters here for other updatable fields

			// Save updated floor
			DocumentType updatedDocumentType = service.updateDocumentType(existingDocumentType.getId(), existingDocumentType);

			return ResponseEntity.ok(updatedDocumentType);

		} catch (Exception e) {
			logger.error("Exception in controller of updatedocumenttype api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the DocumentType");
		}
	}
	
//	@PreAuthorize("hasAuthority('DOCUMENTTYPE_DELETE')")
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTTYPE_DELETE')")
	@DeleteMapping("/admin/deletedocumenttype/{id}")
//	@DeleteMapping("/user/deletefloor/{id}")
	public ResponseEntity<String> deleteDocumentType(@PathVariable("id") Long id) {
		boolean isDeleted = service.deleteDocumentType(id);
		if (isDeleted) {
			String responseContent = "DocumentType has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting DocumentType from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//	@PreAuthorize("hasAuthority('DOCUMENTTYPE_SEARCH')")
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTTYPE_SEARCH')")
	@GetMapping("/user/documenttype/search")
    public List<DocumentType> searchDocumentType(
            @RequestParam(required = false) String shortName,
            @RequestParam(required = false) String documentTypeName,
            @RequestParam(required = false) String documentTypeDescription
            ) {

        return service.search(shortName,documentTypeName,documentTypeDescription);
    }
	
	
	

}
