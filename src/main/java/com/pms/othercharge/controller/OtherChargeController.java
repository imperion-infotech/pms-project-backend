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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.othercharge.entity.OtherCharge;
import com.pms.othercharge.service.IOtherChargeService;
import com.pms.paymenttype.controller.PaymentTypeController;
import com.pms.paymenttype.entity.PaymentType;
import com.pms.util.SnowflakeUtil;

/**
 * 
 */
@RestController
public class OtherChargeController {
	
	private static final Logger logger = LoggerFactory.getLogger(OtherChargeController.class);

	@Autowired
	private IOtherChargeService service;

//	@PreAuthorize("hasAuthority('OTHERCHARGE_VIEW')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHERCHARGE_VIEW')")
	@GetMapping("/user/getothercharges")
	public ResponseEntity<List<OtherCharge>> getOtherCharges() {

		List<OtherCharge> otherCharge = service.getOtherCharges();
		return new ResponseEntity<List<OtherCharge>>(otherCharge, HttpStatus.OK);
	}
	
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHERCHARGE_VIEW')")
//	@PreAuthorize("hasAuthority('OTHERCHARGE_VIEW')")
	@GetMapping("/user/getothercharge/{id}")
	public ResponseEntity<OtherCharge> getOtherCharge(@PathVariable("id") Long id) {
		OtherCharge otherCharge = service.getOtherChargeById(id);
		return new ResponseEntity<OtherCharge>(otherCharge, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAuthority('OTHERCHARGE_CREATE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHERCHARGE_CREATE')")
	@PostMapping("/admin/createothercharge")
	public ResponseEntity<?> createOtherCharge(@RequestBody OtherCharge otherCharge) {
		// Validate input
		if (otherCharge == null || otherCharge.getCategoryName() == null || otherCharge.getCategoryName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("otherCharge CategoryName must not be null or empty");
		}
		
		if (otherCharge == null || otherCharge.getOtherChargeName() == null || otherCharge.getOtherChargeName().toString().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("otherCharge OtherChargeName must not be null or empty");
		}
		
		if (otherCharge == null || otherCharge.getOtherChargeShortName() == null || otherCharge.getOtherChargeShortName().toString().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("otherCharge OtherChargeShortName must not be null or empty");
		}

		if (otherCharge == null || otherCharge.getReoccureChargeFrequency() == null || otherCharge.getReoccureChargeFrequency().toString().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("otherCharge ReoccureChargeFrequency must not be null or empty");
		}
		
		try {
			OtherCharge savedOtherCharge = service.createOtherCharge(otherCharge);
			return ResponseEntity.ok(savedOtherCharge);
		} catch (Exception e) {
			logger.error("Exception in controller of createothercharge api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the OtherCharge");
		}
	}
	
//	@PreAuthorize("hasAuthority('OTHERCHARGE_UPDATE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHERCHARGE_UPDATE')")
	@PutMapping("/admin/updateothercharge/{id}")
	public ResponseEntity<?> updateOtherCharge(@PathVariable Long id, @RequestBody OtherCharge otherChargeDetails) {
		// Validate input
		if (otherChargeDetails == null || otherChargeDetails.getCategoryName() == null || otherChargeDetails.getCategoryName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("otherCharge CategoryName must not be null or empty");
		}

		try {
			OtherCharge existingOtherCharge = service.getOtherChargeById(id);
			if (existingOtherCharge == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" with ID " + id + " not found");
			}

			// Update fields
			existingOtherCharge.setCategoryName(otherChargeDetails.getCategoryName());
			existingOtherCharge.setAlwaysCharge(otherChargeDetails.isAlwaysCharge());
			existingOtherCharge.setCallLoggingCharge(otherChargeDetails.isCallLoggingCharge());
			existingOtherCharge.setOtherChargeName(otherChargeDetails.getOtherChargeName());
			existingOtherCharge.setCallPOSCharge(otherChargeDetails.isCallPOSCharge());
			existingOtherCharge.setCrsCharge(otherChargeDetails.isCrsCharge());
			existingOtherCharge.setForeCastingRevenue(otherChargeDetails.isForeCastingRevenue());
			existingOtherCharge.setReoccureCharge(otherChargeDetails.isReoccureCharge());
			existingOtherCharge.setReoccureChargeFrequency(otherChargeDetails.getReoccureChargeFrequency());
			existingOtherCharge.setOtherChargeShortName(otherChargeDetails.getOtherChargeShortName());
			existingOtherCharge.setTaxable(otherChargeDetails.isTaxable());
			

			OtherCharge updatedOtherCharge = service.updateOtherCharge(existingOtherCharge.getId(), existingOtherCharge);

			return ResponseEntity.ok(updatedOtherCharge);

		} catch (Exception e) {
			logger.error("Exception in controller of updateothercharge api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the OtherCharge");
		}
	}
	
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHERCHARGE_DELETE')")
//	@PreAuthorize("hasAuthority('OTHERCHARGE_DELETE')")
	@DeleteMapping("/admin/deleteothercharge/{id}")
	public ResponseEntity<String> deleteOtherCharge(@PathVariable("id") Long id) {
		boolean isDeleted = service.deleteOtherCharge(id);
		if (isDeleted) {
			String responseContent = "OtherCharge has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting OtherCharge from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//	@PreAuthorize("hasAuthority('OTHERCHARGE_SEARCH')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'OTHERCHARGE_SEARCH')")
	@GetMapping("/user/othercharge/search")
    public List<OtherCharge> searchOtherCharge(
            @RequestParam(required = false) String otherChargeName,
            @RequestParam(required = false) String otherChargeShortName,
            @RequestParam(required = false) String categoryName) {
		
        return service.search(otherChargeName,otherChargeShortName,categoryName);
    }
	


}
