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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.auditlog.util.AuditUtil;
import com.pms.document.entity.DocumentDetails;
import com.pms.document.service.IDocumentDetailsService;
import com.pms.document.service.IDocumentTypeService;

import jakarta.servlet.http.HttpSession;


/**
 * 
 */
@RestController
public class DocumentDetailsController {
	
	

	private static final Logger logger = LoggerFactory.getLogger(DocumentDetailsController.class);

	@Autowired
	private IDocumentDetailsService service;
	
//	@PreAuthorize("hasAuthority('DOCUMENTDETAILS_VIEW')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTDETAILS_VIEW')")
	@GetMapping("/user/getdocumentdetails")
	public ResponseEntity<List<DocumentDetails>> getDocumentDetails() {

		List<DocumentDetails> documentDetails = service.getDocumentDetails();
		return new ResponseEntity<List<DocumentDetails>>(documentDetails, HttpStatus.OK);

	}

//	@PreAuthorize("hasAuthority('DOCUMENTDETAILS_VIEW')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTDETAILS_VIEW')")
	@GetMapping("/user/getdocumentdetail/{id}")
	public ResponseEntity<DocumentDetails> getDocumentDetail(@PathVariable("id") Long id) {
		DocumentDetails documentDetails = service.getDocumentDetail(id);//????
		return new ResponseEntity<DocumentDetails>(documentDetails, HttpStatus.OK);
	}

	
//	@PreAuthorize("hasAuthority('DOCUMENTDETAILS_CREATE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTDETAILS_CREATE')")
	@PostMapping("/admin/createdocumentdetail")
	public ResponseEntity<?> createDocumentDetails( @RequestBody DocumentDetails documentDetails) {
		// Validate input
		if (documentDetails == null || documentDetails.getDocumentNumber() == null || documentDetails.getDocumentNumber().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("documentDetails number must not be null or empty");
		}
		
		try {
			DocumentDetails savedDocumentDetails = service.createDocumentDetails(documentDetails);
//			savedDocumentDetails.set
			return ResponseEntity.ok(savedDocumentDetails);
		} catch (Exception e) {
			// Log the error (optional)
			logger.error("Exception in controller of createdocumentdetail api:"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the floor");
		}
	}

//	@PreAuthorize("hasAuthority('DOCUMENTDETAILS_UPDATE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTDETAILS_UPDATE')")
	@PutMapping("/admin/updatedocumentdetails/{id}")
	public ResponseEntity<?> updateDocumentDetails(@PathVariable Long id, @RequestBody DocumentDetails documentDetails,HttpSession session) {
		// Validate input
		if (documentDetails == null || documentDetails.getDocumentNumber() == null || documentDetails.getDocumentNumber().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("documentDetails number must not be null or empty");
		}
		
//		if (documentDetails == null || documentDetails.getDocumentTypeEnum() == null || documentDetails.getDocumentTypeEnum().toString().trim().isEmpty()) {
//			return ResponseEntity.badRequest().body("documentDetails type must not be null or empty");
//		}


		try {
			// Find existing floor
			DocumentDetails existingDocumentDetails = service.getDocumentDetail(id);
			if (existingDocumentDetails == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DocumentDetails with ID " + id + " not found");
			}
			session.setAttribute("oldValue", AuditUtil.toJson(existingDocumentDetails));
			// Update fields
			existingDocumentDetails.setBackImagePath(documentDetails.getBackImagePath());
			existingDocumentDetails.setFrontImagePath(documentDetails.getFrontImagePath());
			existingDocumentDetails.setDocumentNumber(documentDetails.getDocumentNumber());
			existingDocumentDetails.setDocumentType(documentDetails.getDocumentType());
			existingDocumentDetails.setPersonalDetails(documentDetails.getPersonalDetails());
			existingDocumentDetails.setValidTill(documentDetails.getValidTill());
			existingDocumentDetails.setIsActive(documentDetails.getIsActive());
			existingDocumentDetails.setIsDeleted(documentDetails.getIsDeleted());

			DocumentDetails updatedDocumentDetails = service.updateDocumentDetails(existingDocumentDetails.getId(), existingDocumentDetails);

			return ResponseEntity.ok(updatedDocumentDetails);

		} catch (Exception e) {
			logger.error("Exception in controller of updatedocumentdetail api:"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the floor");
		}
	}

//	@PreAuthorize("hasAuthority('DOCUMENTDETAILS_DELETE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTDETAILS_DELETE')")
	@DeleteMapping("/admin/deletedocumentdetails/{id}")
	public ResponseEntity<String> deleteDocumentDetails(@PathVariable("id") Long id) {
		boolean isDeleted = service.deleteDocumentDetails(id);
		if (isDeleted) {
			String responseContent = "DocumentDetails has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting Building from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//	@PreAuthorize("hasAuthority('DOCUMENTDETAILS_SEARCH')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'DOCUMENTDETAILS_SEARCH')")
	@GetMapping("/user/documentdetails/search")
    public List<DocumentDetails> searchDocumentDetails(
            @RequestParam(required = false) String documentTypeEnum,
            @RequestParam(required = false) String documentNumber) {

        return service.search(documentTypeEnum,documentNumber);
    }
	

}
