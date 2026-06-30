/**
 * 
 */
package com.pms.roomtype.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.roomstatus.entity.RoomStatus;
import com.pms.roomtype.entity.RoomType;
import com.pms.roomtype.services.IRoomTypeService;

/**
 * 
 */
@RestController
public class RoomTypeController {

	private static final Logger logger = LoggerFactory.getLogger(RoomTypeController.class);

	@Autowired
	private IRoomTypeService service;

	@GetMapping("/user/getroomtypes")
//	@PreAuthorize("hasAuthority('ROOMTYPE_VIEW')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMTYPE_VIEW')")
	public ResponseEntity<List<RoomType>> getRoomTypes() {

		List<RoomType> roomTypes = service.getRoomTypes();
		return new ResponseEntity<List<RoomType>>(roomTypes, HttpStatus.OK);

	}

	@GetMapping("/user/getroomtype/{id}")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMTYPE_VIEW')")
//	@PreAuthorize("hasAuthority('ROOMTYPE_VIEW')")
	public ResponseEntity<RoomType> getRoomType(@PathVariable("id") Long id) {
		RoomType roomType = service.getRoomType(id);
		return new ResponseEntity<RoomType>(roomType, HttpStatus.OK);
	}

	@PostMapping("/admin/createroomtype")
//	@PreAuthorize("hasAuthority('ROOMTYPE_CREATE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMTYPE_CREATE')")
	public ResponseEntity<?> createRoomType(@RequestBody RoomType roomType) {
		// Validate input
		if (roomType == null || roomType.getShortName() == null || roomType.getShortName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("RoomType shortname must not be null or empty");
		}

		if (roomType == null || roomType.getRoomTypeName() == null || roomType.getRoomTypeName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("RoomType roomtyname must not be null or empty");
		}
		
//		if (roomType == null || roomType.getPrice() == null || roomType.getPrice()==0L) {
//			return ResponseEntity.badRequest().body("RoomType price must not be null or empty");
//		}

		try {
			RoomType savedRoomType = service.createRoomType(roomType);
			return ResponseEntity.ok(savedRoomType);
		} catch (Exception e) {
			logger.error("Exception in controller of createRoomType api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the RoomType");
		}
	}

@PutMapping("/admin/updateroomtype/{id}")
@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMTYPE_UPDATE')")
//@PreAuthorize("hasAuthority('ROOMTYPE_UPDATE')")
	public ResponseEntity<?> updateRoomType(@PathVariable Long id, @RequestBody RoomType roomTypeDetails) {
		// Validate input
		if (roomTypeDetails == null || roomTypeDetails.getShortName() == null
				|| roomTypeDetails.getShortName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("RoomType shortname must not be null or empty");
		}

		if (roomTypeDetails == null || roomTypeDetails.getRoomTypeName() == null
				|| roomTypeDetails.getRoomTypeName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("RoomType roomtypename must not be null or empty");
		}
		
		if (roomTypeDetails == null || roomTypeDetails.getPrice() == null || roomTypeDetails.getPrice()==0L) {
			return ResponseEntity.badRequest().body("RoomType price must not be null or empty");
		}

		try {
			// Find existing RoomType
			RoomType existingRoomType = service.getRoomTypeById(id);
			if (existingRoomType == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RoomType with ID " + id + " not found");
			}

			// Update fields
			existingRoomType.setShortName(roomTypeDetails.getShortName());
			existingRoomType.setRoomTypeName(roomTypeDetails.getRoomTypeName());
			existingRoomType.setPrice(roomTypeDetails.getPrice());
			// You can add more setters here for other updatable fields

			// Save updated RoomType
			RoomType updatedRoomType = service.updateRoomType(existingRoomType.getId(), existingRoomType);

			return ResponseEntity.ok(updatedRoomType);

		} catch (Exception e) {
			logger.error("Exception in controller of updateroomtype api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the RoomType");
		}
	}

	@DeleteMapping("/admin/deleteroomtype/{id}")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMTYPE_DELETE')")
//	@PreAuthorize("hasAuthority('ROOMTYPE_DELETE')")
//	@DeleteMapping("/auth/deleteroomtype/{id}")
	public ResponseEntity<String> deleteRoomType(@PathVariable("id") Long id) {
		boolean isDeleted = service.deleteRoomType(id);
		if (isDeleted) {
			String responseContent = "RoomType has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting RoomType from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//	@PreAuthorize("hasAuthority('ROOMTYPE_SEARCH')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMTYPE_SEARCH')")
	@GetMapping("/user/roomtype/search")
    public List<RoomType> searchRoomType(
            @RequestParam(required = false) String shortName,
            @RequestParam(required = false) String roomTypeName
            ) {

        return service.search(shortName,roomTypeName);
    }

}
