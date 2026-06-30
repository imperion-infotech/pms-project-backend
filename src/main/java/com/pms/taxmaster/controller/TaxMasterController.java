/**
 * 
 */
package com.pms.taxmaster.controller;

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

import com.pms.building.entity.Building;
import com.pms.taxmaster.entity.TaxMaster;
import com.pms.taxmaster.service.ITaxMasterService;

/**
 * 
 */
@RestController
public class TaxMasterController {
	

	private static final Logger logger = LoggerFactory.getLogger(TaxMasterController.class);

	@Autowired
	private ITaxMasterService service;

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TAXMASTER_VIEW')")
	@GetMapping("/user/gettaxmasters")
	public ResponseEntity<List<TaxMaster>> getTaxMasters() {

		List<TaxMaster> taxMasters = service.getTaxMasters();
		return new ResponseEntity<List<TaxMaster>>(taxMasters, HttpStatus.OK);

	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TAXMASTER_VIEW')")
	@GetMapping("/user/gettaxmaster/{id}")
	public ResponseEntity<TaxMaster> getTaxMaster(@PathVariable("id") Integer id) {
		TaxMaster taxMaster = service.getTaxMaster(id);
		return new ResponseEntity<TaxMaster>(taxMaster, HttpStatus.OK);
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TAXMASTER_CREATE')")
	@PostMapping("/admin/createtaxmaster")
	public ResponseEntity<?> createTaxMaster(@RequestBody TaxMaster taxMaster) {
		// Validate input
		if (taxMaster == null || taxMaster.getTaxMasterName() == null || taxMaster.getTaxMasterName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("taxMaster name must not be null or empty");
		}
		
		if (taxMaster == null || taxMaster.getTaxTypeEnum() == null || taxMaster.getTaxTypeEnum().toString().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("taxMaster name must not be null or empty");
		}

		try {
			TaxMaster savedTaxMaster = service.createTaxMaster(taxMaster);
			return ResponseEntity.ok(savedTaxMaster);
		} catch (Exception e) {
			logger.error("Exception in controller of createtaxmaster api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the floor");
		}
	}

	@PutMapping("/admin/updatetaxmaster/{id}")
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'TAXMASTER_UPDATE')")
	public ResponseEntity<?> updateTaxMaster(@PathVariable Integer id, @RequestBody TaxMaster taxMasterDetails) {
		// Validate input
		if (taxMasterDetails == null || taxMasterDetails.getTaxMasterName() == null || taxMasterDetails.getTaxMasterName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("TaxMaster name must not be null or empty");
		}

		try {
			// Find existing floor
			TaxMaster existingTaxMaster = service.getTaxMasterByIdAndHotelID(id);
			if (existingTaxMaster == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TaxMaster with ID " + id + " not found");
			}

			// Update fields
			existingTaxMaster.setTaxMasterName(taxMasterDetails.getTaxMasterName());
			existingTaxMaster.setTaxTypeEnum(taxMasterDetails.getTaxTypeEnum());
			existingTaxMaster.setPerDayTax(taxMasterDetails.getPerDayTax());
			existingTaxMaster.setPerStayTax(taxMasterDetails.getPerStayTax());
			existingTaxMaster.setAmount(taxMasterDetails.getAmount());

			// Save updated floor
			TaxMaster updatedTaxMaster = service.updateTaxMaster(existingTaxMaster.getId(), existingTaxMaster);

			return ResponseEntity.ok(updatedTaxMaster);

		} catch (Exception e) {
			logger.error("Exception in controller of updateTaxMaster api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the taxmaster");
		}
	}

	@DeleteMapping("/admin/deletetaxmaster/{id}")
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'TAXMASTER_DELETE')")
	public ResponseEntity<String> deleteTaxMaster(@PathVariable("id") int id) {
		boolean isDeleted = service.deleteTaxMaster(id);
		if (isDeleted) {
			String responseContent = "TaxMaster has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting TaxMaster from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'TAXMASTER_SEARCH')")
	@GetMapping("/user/taxmaster/search")
    public List<TaxMaster> searchTaxMaster(
            @RequestParam(required = false) String taxMasterName,
            @RequestParam(required = false) String taxTypeEnum) {

        return service.search(taxMasterName,taxTypeEnum);
    }
}
