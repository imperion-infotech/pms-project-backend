/**
 * 
 */
package com.pms.stay.controller;

import java.time.LocalDateTime;
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
import com.pms.room.entity.RoomMaster;
import com.pms.stay.dto.ChangeRoomRequestDTO;
import com.pms.stay.entity.StayDetails;
import com.pms.stay.service.IStayDetailsService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

/**
 * 
 */
@RestController
public class StayDetailsController {

	private static final Logger logger = LoggerFactory.getLogger(StayDetailsController.class);

	@Autowired
	private IStayDetailsService service;

	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'STAYDETAILS_VIEW')")
//	@PreAuthorize("hasAuthority('STAYDETAILS_VIEW')")
	@GetMapping("/user/getstaydetails")
	public ResponseEntity<List<StayDetails>> getStayDetails() {
		List<StayDetails> stayDetails = service.getStayDetails();
		return new ResponseEntity<List<StayDetails>>(stayDetails, HttpStatus.OK);

	}

	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'STAYDETAILS_VIEW')")
//	@PreAuthorize("hasAuthority('STAYDETAILS_VIEW')")
	@GetMapping("/user/getstaydetail/{id}")
	public ResponseEntity<StayDetails> getStayDetails(@PathVariable("id") Long id) {
		StayDetails stayDetails = service.getStayDetail(id);
		return new ResponseEntity<StayDetails>(stayDetails, HttpStatus.OK);
	}

	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'STAYDETAILS_CREATE')")
//	@PreAuthorize("hasAuthority('STAYDETAILS_CREATE')")
	@PostMapping("/admin/createstaydetail")
	public ResponseEntity<?> createStayDetails(@RequestBody StayDetails stayDetails) {
		// Validate input
//		if (stayDetails == null || stayDetails.getFloorId() == null) {
//			return ResponseEntity.badRequest().body("StayDetails name must not be null or empty");
//		}

		if (stayDetails == null || stayDetails.getRoomTypeId() == null) {
			return ResponseEntity.badRequest().body("StayDetails title must not be null or empty");
		}

//		if (stayDetails == null || stayDetails.getBuildingId() == null || stayDetails.getBuildingId() == 0l) {
//
//			return ResponseEntity.badRequest().body("StayDetails color must not be null or empty");
//		}

//		if (stayDetails == null || stayDetails.getFloorId() == 0) {
//			return ResponseEntity.badRequest().body("StayDetails floorid must not be null or empty");
//		}

		if (stayDetails == null || stayDetails.getRoomTypeId() == 0) {
			return ResponseEntity.badRequest().body("StayDetails RoomTypeId must not be null or empty");
		}

//		if (stayDetails == null || stayDetails.getRoomMasterId() == 0) {
//			return ResponseEntity.badRequest().body("StayDetails roomMasterId must not be null or empty");
//		}

//		if (stayDetails == null || stayDetails.getNoOfGuest() == 0L) {
//			return ResponseEntity.badRequest().body("StayDetails no of guest must not be zero or empty");
//		}

		try {
			StayDetails savedStayDetails = service.createStayDetails(stayDetails);
			return ResponseEntity.ok(savedStayDetails);
		} catch (Exception e) {
			logger.error("Exception in controller of createstaydetail api :" + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the roomMaster");
		}
	}

	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'STAYDETAILS_UPDATE')")
//	@PreAuthorize("hasAuthority('STAYDETAILS_UPDATE')")
	@PutMapping("/admin/updatestaydetails/{id}")
	public ResponseEntity<?> updateStayDetails(@PathVariable Long id, @RequestBody StayDetails stayDetails,
			HttpSession session) {
		// Validate input
//		if (stayDetails == null || stayDetails.getFloorId() == null) {
//			return ResponseEntity.badRequest().body("StayDetails name must not be null or empty");
//		}

		if (stayDetails == null || stayDetails.getRoomTypeId() == null) {
			return ResponseEntity.badRequest().body("StayDetails title must not be null or empty");
		}

//		if (stayDetails == null || stayDetails.getBuildingId() == null || stayDetails.getBuildingId() == 0l) {
//
//			return ResponseEntity.badRequest().body("StayDetails building must not be null or empty");
//		}
//
//		if (stayDetails == null || stayDetails.getFloorId() == 0) {
//			return ResponseEntity.badRequest().body("StayDetails floorid must not be null or empty");
//		}

		if (stayDetails == null || stayDetails.getRoomTypeId() == 0) {
			return ResponseEntity.badRequest().body("StayDetails RoomTypeId must not be null or empty");
		}

//		if (stayDetails == null || stayDetails.getRoomMasterId() == 0) {
//			return ResponseEntity.badRequest().body("StayDetails roomMasterId must not be null or empty");
//		}

//		if (stayDetails == null || stayDetails.getRateTypeEnum().toString().isEmpty()) {
//			return ResponseEntity.badRequest().body("StayDetails roomMasterId must not be null or empty");
//		}
//
//		if (stayDetails == null || stayDetails.getStayStatusEnum().toString().isEmpty()) {
//			return ResponseEntity.badRequest().body("StayDetails roomMasterId must not be null or empty");
//		}
//
//		if (stayDetails == null || stayDetails.getComment().trim().isEmpty()) {
//			return ResponseEntity.badRequest().body("StayDetails comment must not be null or empty");
//		}
//
//		if (stayDetails == null || stayDetails.getNoOfGuest() == 0L) {
//			return ResponseEntity.badRequest().body("StayDetails no of guest must not be zero or empty");
//		}

		try {
			// Find existing RoomType
			StayDetails existingStayDetails = service.getStayDetail(id);
			session.setAttribute("oldValue", AuditUtil.toJson(existingStayDetails));
			if (existingStayDetails == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("StayDetails with ID " + id + " not found");
			}

			// Update fields
			existingStayDetails.setFloorId(stayDetails.getFloorId());
			existingStayDetails.setBuildingId(stayDetails.getBuildingId());
			existingStayDetails.setComment(stayDetails.getComment());
			existingStayDetails.setRoomTypeId(stayDetails.getRoomTypeId());

			existingStayDetails.setFloorId(stayDetails.getFloorId());
			existingStayDetails.setNoOfGuest(stayDetails.getNoOfGuest());
			existingStayDetails.setRateTypeEnum(stayDetails.getRateTypeEnum());
			existingStayDetails.setStayStatusEnum(stayDetails.getStayStatusEnum());
			existingStayDetails.setTaxExempt(stayDetails.getTaxExempt());
			// You can add more setters here for other updatable fields

			// Save updated RoomType
			StayDetails updatedStayDetails = service.updateStayDetails(Long.valueOf(existingStayDetails.getId()),
					existingStayDetails);

			return ResponseEntity.ok(updatedStayDetails);

		} catch (Exception e) {
			logger.error("Exception in controller of updateStayDetails api :" + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the roomMaster");
		}
	}

	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'STAYDETAILS_DELETE')")
//	@PreAuthorize("hasAuthority('STAYDETAILS_DELETE')")
	@DeleteMapping("/admin/deletestaydetails/{id}")
	public ResponseEntity<String> deleteStayDetails(@PathVariable("id") Long id) {
		boolean isDeleted = service.deleteSoftStayDetails(id);
		if (isDeleted) {
			String responseContent = "StayDetails has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting StayDetails from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOM_CHANGE')")
//	@PreAuthorize("hasAuthority('ROOM_CHANGE')")
	@PostMapping("/change-room")
	public ResponseEntity<String> changeRoom(@Valid @RequestBody ChangeRoomRequestDTO dto) {

		service.changeRoom(dto);

		return ResponseEntity.ok("Room changed successfully");
	}

}
