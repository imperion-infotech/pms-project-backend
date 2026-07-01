/**
 * 
 */
package com.pms.othercharge.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.pms.othercharge.entity.OtherChargeDetails;
import com.pms.othercharge.entity.OtherChargeDetailsResponseDTO;
import com.pms.othercharge.service.IOtherChargeDetailsService;

/**
 * 
 */
@RestController
public class OtherChargeDetailsController {
	
	private static final Logger logger = LoggerFactory.getLogger(OtherChargeDetailsController.class);

	@Autowired
	private IOtherChargeDetailsService service;
	
//	@PreAuthorize("hasAuthority('OTHER_CHARGE_DETAILS_VIEW')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHER_CHARGE_DETAILS_VIEW')")
	@GetMapping("/user/getallotherchargedetails")
	public ResponseEntity<List<OtherChargeDetails>> getOtherChargeDetails() {
		List<OtherChargeDetails> otherChargeDetails = service.getAllOtherChargeDetails();
		return new ResponseEntity<List<OtherChargeDetails>>(otherChargeDetails, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAuthority('OTHER_CHARGE_DETAILS_VIEW')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHER_CHARGE_DETAILS_VIEW')")
	@GetMapping("/user/getotherchargedetails/{id}")
	public ResponseEntity<OtherChargeDetails> getOtherChargeDetails(@PathVariable("id") Long id) {
		OtherChargeDetails otherChargeDetails = service.getOtherChargeDetailsById(id);
		return new ResponseEntity<OtherChargeDetails>(otherChargeDetails, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAuthority('OTHER_CHARGE_DETAILS_CREATE')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHER_CHARGE_DETAILS_CREATE')")
	@PostMapping("/admin/createotherchargedetails")
	public ResponseEntity<?> createOtherChargeDetails(@RequestBody OtherChargeDetails otherChargeDetails) {
		// Validate input
		//		if (otherChargeDetails == null || otherChargeDetails.getTotalCharges() == null || otherChargeDetails.getTotalCharges() == 0) {
//			return ResponseEntity.badRequest().body("otherChargeDetails TotalCharges must not be null or zero");
//		}
		
		if (otherChargeDetails == null || otherChargeDetails.isDisplayOnFolio() == null ) {
			return ResponseEntity.badRequest().body("otherChargeDetails DisplayOnFolio must not be null or empty");
		}

		
		try {
			OtherChargeDetails savedOtherChargeDetails = service.createOtherChargeDetails(otherChargeDetails);
			return ResponseEntity.ok(savedOtherChargeDetails);
		} catch (Exception e) {
			logger.error("Exception in controller of createotherchargedetails api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the OtherChargeDetails");
		}
	}
	
	
//	@PreAuthorize("hasAuthority('OTHER_CHARGE_DETAILS_UPDATE')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHER_CHARGE_DETAILS_UPDATE')")
	@PutMapping("/admin/updateotherchargedetails/{id}")
	public ResponseEntity<?> updateOtherChargeDetails(@PathVariable Long id, @RequestBody OtherChargeDetails otherChargeDetails) {
		
		logger.info("updateOtherChargeDetails API called");
		

		try {
			OtherChargeDetails existingOtherChargeDetails = service.getOtherChargeDetailsById(id);
			if (existingOtherChargeDetails == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" with ID " + id + " not found");
			}

			// Update fields
			existingOtherChargeDetails.setRemark(otherChargeDetails.getRemark());
			existingOtherChargeDetails.setDisplayOnFolio(otherChargeDetails.isDisplayOnFolio());
			existingOtherChargeDetails.setTotalCharges(otherChargeDetails.getTotalCharges());
			existingOtherChargeDetails.setAmount(otherChargeDetails.getAmount());
			
			existingOtherChargeDetails.setIsRefund(otherChargeDetails.getIsRefund());
			existingOtherChargeDetails.setRefundAmount(otherChargeDetails.getRefundAmount());
			existingOtherChargeDetails.setRefundType(otherChargeDetails.getRefundType());
			existingOtherChargeDetails.setTransactionId(otherChargeDetails.getTransactionId());
			existingOtherChargeDetails.setRefundAccountNo(otherChargeDetails.getRefundAccountNo());
			existingOtherChargeDetails.setGuestDetails(otherChargeDetails.getGuestDetails());
			
			
			OtherChargeDetails updatedOtherChargeDetails = service.updateOtherChargeDetails(existingOtherChargeDetails.getId(), existingOtherChargeDetails);

			return ResponseEntity.ok(updatedOtherChargeDetails);

		} catch (Exception e) {
			logger.error("Exception in controller of updateotherchargedetails api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the updatedOtherChargeDetails");
		}
	}
	
//	@PreAuthorize("hasAuthority('OTHER_CHARGE_DETAILS_DELETE')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHER_CHARGE_DETAILS_DELETE')")
	@DeleteMapping("/admin/deleteotherchargedetails/{id}")
	public ResponseEntity<String> deleteOtherChargeDetails(@PathVariable("id") Long id) {
		boolean isDeleted = service.deleteOtherChargeDetails(id);
		if (isDeleted) {
			String responseContent = "OtherChargeDetails has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting OtherChargeDetails from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//	@PreAuthorize("hasAuthority('OTHER_CHARGE_DETAILS_VIEW')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHER_CHARGE_DETAILS_VIEW')")
	@GetMapping("/user/other-charge-details/{guestId}")
	public ResponseEntity<List<OtherChargeDetailsResponseDTO>> getOtherChargeDetailsByGuest(@PathVariable Long guestId) {

		List<OtherChargeDetailsResponseDTO> response = service.getOtherChargeDetailsByGuestId(guestId);

		return ResponseEntity.ok(response);
	}
	
	

}
