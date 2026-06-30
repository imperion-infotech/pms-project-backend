/**
 * 
 */
package com.pms.building.controller;

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
import com.pms.building.entity.Building;
import com.pms.building.service.IBuildingService;

import jakarta.servlet.http.HttpSession;

/**
 * 
 */
@RestController
public class BuildingController {
	

	private static final Logger logger = LoggerFactory.getLogger(BuildingController.class);

	@Autowired
	private IBuildingService service;

	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'BUILDING_VIEW')")
//	@PreAuthorize("hasAnyRole('SUPER_ADMIN','HOTEL_OWNER','USER')")
	@GetMapping("/user/getbuildings")
	public ResponseEntity<List<Building>> getBuildings() {
       logger.info("==== building lists =====");
		List<Building> buildings = service.getBuildings();
		return new ResponseEntity<List<Building>>(buildings, HttpStatus.OK);

	}

	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'BUILDING_VIEW')")
	@GetMapping("/user/getbuilding/{id}")
	public ResponseEntity<Building> getBuilding(@PathVariable("id") Long id) {
		Building building = service.getBuilding(id);
		return new ResponseEntity<Building>(building, HttpStatus.OK);
	}

	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'BUILDING_CREATE')")
//	@PreAuthorize("hasAnyRole('SUPER_ADMIN','HOTEL_OWNER')")
	@PostMapping("/admin/createbuilding")
	public ResponseEntity<?> createBuilding(@RequestBody Building building) {
		if (building == null || building.getName() == null || building.getName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("building name must not be null or empty");
		}
		
		if (building == null || building.getDescription() == null || building.getDescription().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("building getDescription must not be null or empty");
		}

		try {
			Building savedBuilding = service.createBuilding(building);
			return ResponseEntity.ok(savedBuilding);
		} catch (Exception e) {
			logger.error("Exception in createbuilding api:"+e);
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the BUILDING");
		}
	}

	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'BUILDING_UPDATE')")
	@PutMapping("/admin/updatebuilding/{id}")
	public ResponseEntity<?> updateBuilding(@PathVariable Long id, @RequestBody Building buildingDetails,HttpSession session) {
		// Validate input
		if (buildingDetails == null || buildingDetails.getName() == null || buildingDetails.getName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Building name must not be null or empty");
		}
		if (buildingDetails == null || buildingDetails.getDescription() == null || buildingDetails.getDescription().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("building getDescription must not be null or empty");
		}

		try {
			// Find existing floor
			Building existingBuilding = service.getBuildingById(id);
			session.setAttribute("oldValue", AuditUtil.toJson(existingBuilding));
			
			if (existingBuilding == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Floor with ID " + id + " not found");
			}

			// Update fields
			existingBuilding.setName(buildingDetails.getName());
			existingBuilding.setDescription(buildingDetails.getDescription());

			// You can add more setters here for other updatable fields

			// Save updated floor
			Building updatedBuilding = service.updateBuilding(existingBuilding.getId(), existingBuilding);

			return ResponseEntity.ok(updatedBuilding);

		} catch (Exception e) {
			logger.error("Exception in updatebuilding api:"+e);
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the floor");
		}
	}

	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'BUILDING_DELETE')")
	@DeleteMapping("/admin/deletebuilding/{id}")
	public ResponseEntity<String> deleteBuilding(@PathVariable("id") Long id) {
		boolean isDeleted = service.deleteBuilding(id);
		if (isDeleted) {
			String responseContent = "Building has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting Building from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'BUILDING_SEARCH')")
	@GetMapping("/buildingsearch")
    public List<Building> searchBuilding(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        return service.search(name,description);
    }
	
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'BUILDING_DELETE')")
	@DeleteMapping("/user/deleteBuildingHierarchy/{buildingId}")
	public ResponseEntity<String> deleteBuildingHierarchy(@PathVariable Long buildingId) {

	    service.deleteBuildingHierarchy(buildingId);

	    return ResponseEntity.ok("Building deleted successfully");
	}
	

}
