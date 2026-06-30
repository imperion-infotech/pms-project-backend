/**
 * 
 */
package com.pms.roomstatus.controller;

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

import com.pms.floor.entity.Floor;
import com.pms.roomstatus.entity.RoomStatus;
import com.pms.roomstatus.services.IRoomStatusService;

/**
 * 
 */
@RestController
public class RoomStatusController {

	private static final Logger logger = LoggerFactory.getLogger(RoomStatusController.class);

	@Autowired
	private IRoomStatusService service;

	@GetMapping("/user/getroomstatuses")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMSTATUS_VIEW')")
//	@PreAuthorize("hasAuthority('ROOMSTATUS_VIEW')")
	public ResponseEntity<List<RoomStatus>> getRoomStatus() {

		List<RoomStatus> roomStatuses = service.getRoomStatuses();
		return new ResponseEntity<List<RoomStatus>>(roomStatuses, HttpStatus.OK);

	}

	@GetMapping("/user/getroomstatus/{id}")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMSTATUS_VIEW')")
//	@PreAuthorize("hasAuthority('ROOMSTATUS_VIEW')")
	public ResponseEntity<RoomStatus> getRoomStatus(@PathVariable("id") Long id) {
		RoomStatus roomStatus = service.getRoomStatus(id);
		return new ResponseEntity<RoomStatus>(roomStatus, HttpStatus.OK);
	}

	@PostMapping("/admin/createroomstatus")
//	@PreAuthorize("hasAuthority('ROOMSTATUS_CREATE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMSTATUS_CREATE')")
	public ResponseEntity<?> createRoomType(@RequestBody RoomStatus roomStatus) {
		// Validate input
		if (roomStatus == null || roomStatus.getRoomStatusName() == null
				|| roomStatus.getRoomStatusName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("RoomStatus name must not be null or empty");
		}

		if (roomStatus == null || roomStatus.getRoomStatusTitle() == null
				|| roomStatus.getRoomStatusTitle().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("roomStatus title must not be null or empty");
		}

		if (roomStatus == null || roomStatus.getRoomStatusColor() == null
				|| roomStatus.getRoomStatusColor().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("roomStatus color must not be null or empty");
		}

		if (roomStatus == null || roomStatus.getRoomStatusTextColor() == null
				|| roomStatus.getRoomStatusTextColor().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("roomStatus text color must not be null or empty");
		}

		try {
			RoomStatus savedRoomStatus = service.createRoomStatus(roomStatus);
			return ResponseEntity.ok(savedRoomStatus);
		} catch (Exception e) {
			logger.error("Exception in controller of createRoomType api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the RoomType");
		}
	}

@PutMapping("/admin/updateroomstatus/{id}")
@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMSTATUS_UPDATE')")
//@PreAuthorize("hasAuthority('ROOMSTATUS_UPDATE')")
	public ResponseEntity<?> updateRoomType(@PathVariable Long id, @RequestBody RoomStatus roomStatusDetails) {
		// Validate input
		if (roomStatusDetails == null || roomStatusDetails.getRoomStatusName() == null
				|| roomStatusDetails.getRoomStatusName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("RoomStatus name must not be null or empty");
		}

		if (roomStatusDetails == null || roomStatusDetails.getRoomStatusTitle() == null
				|| roomStatusDetails.getRoomStatusTitle().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("roomStatus title must not be null or empty");
		}

		if (roomStatusDetails == null || roomStatusDetails.getRoomStatusColor() == null
				|| roomStatusDetails.getRoomStatusColor().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("roomStatus color must not be null or empty");
		}

		if (roomStatusDetails == null || roomStatusDetails.getRoomStatusTextColor() == null
				|| roomStatusDetails.getRoomStatusTextColor().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("roomStatus text color must not be null or empty");
		}

		try {
			// Find existing RoomType
			RoomStatus existingRoomStatus = service.getRoomStatusById(id);
			if (existingRoomStatus == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RoomStatus with ID " + id + " not found");
			}
			// Update fields
			existingRoomStatus.setRoomStatusColor(roomStatusDetails.getRoomStatusColor());
			existingRoomStatus.setRoomStatusName(roomStatusDetails.getRoomStatusName());
			existingRoomStatus.setRoomStatusTextColor(roomStatusDetails.getRoomStatusTextColor());
			existingRoomStatus.setRoomStatusTitle(roomStatusDetails.getRoomStatusTitle());
			// You can add more setters here for other updatable fields

			// Save updated RoomType
			RoomStatus updatedRoomStatus = service.updateRoomStatus(existingRoomStatus.getId(), existingRoomStatus);

			return ResponseEntity.ok(updatedRoomStatus);

		} catch (Exception e) {
			logger.error("Exception in controller of updateRoomType api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the RoomType");
		}
	}

//	@PreAuthorize("hasAuthority('ROOMSTATUS_DELETE')")
@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMSTATUS_DELETE')")
	@DeleteMapping("/admin/deleteroomstatus/{id}")
	public ResponseEntity<String> deleteRoomStatus(@PathVariable("id") Long id) {
		boolean isDeleted = service.deleteRoomStatus(id);
		if (isDeleted) {
			String responseContent = "RoomStatus has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting RoomStatus from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//	@PreAuthorize("hasAuthority('ROOMSTATUS_SEARCH')")
@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMSTATUS_SEARCH')")
	@GetMapping("/user/roomstatus/search")
    public List<RoomStatus> searchRoomStatus(
            @RequestParam(required = false) String roomStatusName,
            @RequestParam(required = false) String roomStatusDescription) {

        return service.search(roomStatusName,roomStatusDescription);
    }

}
