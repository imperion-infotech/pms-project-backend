/**
 * 
 */
package com.pms.rent.controller;

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

import com.pms.auditlog.util.AuditUtil;
import com.pms.rent.RentDetails;
import com.pms.rent.services.IRentDetailsService;

import jakarta.servlet.http.HttpSession;

/**
 * 
 */
@RestController
public class RentDetailsController {
	
	private static final Logger logger = LoggerFactory.getLogger(RentDetailsController.class);

	@Autowired
	private IRentDetailsService service;

//	@PreAuthorize("hasAuthority('RENTDETAILS_VIEW')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'RENTDETAILS_VIEW')")
	@GetMapping("/user/getrentdetails")
	public ResponseEntity<List<RentDetails>> getRentDetails() {

		List<RentDetails> rentDetails = service.getRentDetails();
		return new ResponseEntity<List<RentDetails>>(rentDetails, HttpStatus.OK);

	}
	
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'RENTDETAILS_VIEW')")
//	@PreAuthorize("hasAuthority('RENTDETAILS_VIEW')")
	@GetMapping("/user/getrentdetail/{id}")
	public ResponseEntity<RentDetails> getRentDetail(@PathVariable("id") Long id) {
		RentDetails rentDetails = service.getRentDetail(id);
		return new ResponseEntity<RentDetails>(rentDetails, HttpStatus.OK);
	}
	
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'RENTDETAILS_CREATE')")
//	@PreAuthorize("hasAuthority('RENTDETAILS_CREATE')")
	@PostMapping("/admin/createrentdetail")
	public ResponseEntity<?> createRentDetails(@RequestBody RentDetails rentDetail) {
		try {
			RentDetails savedRentDetails = service.createRentDetail(rentDetail);
			return ResponseEntity.ok(savedRentDetails);
		} catch (Exception e) {
			logger.error("Exception in controller of createrentdetail api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the RoomType");
		}
	}
	
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'RENTDETAILS_UPDATE')")
//	@PreAuthorize("hasAuthority('RENTDETAILS_UPDATE')")
	@PutMapping("/admin/updaterentdetail/{id}")
	public ResponseEntity<?> updateRentDetails(@PathVariable Long id, @RequestBody RentDetails rentDetails, HttpSession session) {
		// Validate input
		
		try {
			// Find existing RoomType
			RentDetails existingRentDetails = service.getRentDetail(id);
			if (existingRentDetails == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RentDetails with ID " + id + " not found");
			}
			session.setAttribute("oldValue", AuditUtil.toJson(existingRentDetails));
			// Update fields
			existingRentDetails.setBalance(rentDetails.getBalance());
			existingRentDetails.setBasic(rentDetails.getBasic());
			existingRentDetails.setCcAuthorized(rentDetails.getCcAuthorized());
			existingRentDetails.setDeposite(rentDetails.getDeposite());
			existingRentDetails.setDiscount(rentDetails.getDiscount());
			existingRentDetails.setOtherCharges(rentDetails.getOtherCharges());
			existingRentDetails.setPayments(rentDetails.getPayments());
			existingRentDetails.setRent(rentDetails.getRent());
			existingRentDetails.setTaxId(rentDetails.getTaxId());
			existingRentDetails.setTaxMaster(rentDetails.getTaxMaster());
			existingRentDetails.setTotalCharges(rentDetails.getTotalCharges());
			existingRentDetails.setTotalRental(rentDetails.getTotalRental());
			existingRentDetails.setCarryForwardAmount(rentDetails.getCarryForwardAmount());
			// Save updated RoomType
			RentDetails updatedRentDetails = service.updateRentDetail(existingRentDetails.getId(), existingRentDetails);

			return ResponseEntity.ok(updatedRentDetails);

		} catch (Exception e) {
			logger.error("Exception in controller of updateRentDetails api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the RoomType");
		}
	}
	
//	@PreAuthorize("hasAuthority('RENTDETAILS_DELETE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'RENTDETAILS_DELETE')")
	@DeleteMapping("/admin/deleterentdetail/{id}")
	public ResponseEntity<String> deleteRentDetail(@PathVariable("id") Long id) {
		boolean isDeleted = service.deleteSoftRentDetails(id);
		if (isDeleted) {
			String responseContent = "RentDetails has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting RentDetails from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
