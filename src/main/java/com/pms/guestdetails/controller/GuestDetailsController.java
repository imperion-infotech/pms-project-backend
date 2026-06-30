/**
 * 
 */
package com.pms.guestdetails.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dto.RoomActivityResponseDTO;
import com.pms.guestdetails.service.IGuestDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;

/**
 * 
 */
@RestController
public class GuestDetailsController {

	private static final Logger logger = LoggerFactory.getLogger(GuestDetailsController.class);

	@Autowired
	private IGuestDetailsService service;

//	@PreAuthorize("hasAuthority('GUESTDETAILS_VIEW')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'GUESTDETAILS_VIEW')")
	@GetMapping("/user/getguestdetails")
	public ResponseEntity<List<GuestDetails>> getGuestDetails() {

		List<GuestDetails> guestDetails = service.getGuestDetails();
		return new ResponseEntity<List<GuestDetails>>(guestDetails, HttpStatus.OK);
	}

//	@PreAuthorize("hasAuthority('GUESTDETAILS_VIEW')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'GUESTDETAILS_VIEW')")
	@GetMapping("/user/getguestdetail/{id}")
	public ResponseEntity<GuestDetails> getGuestDetail(@PathVariable("id") Long id) {
		GuestDetails guestDetails = service.getGuestDetail(id);
		return new ResponseEntity<GuestDetails>(guestDetails, HttpStatus.OK);
	}

//	@PreAuthorize("hasAuthority('GUESTDETAILS_CREATE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'GUESTDETAILS_CREATE')")
	@PostMapping("/admin/createguestdetail")
	public ResponseEntity<?> createGuestDetails(@RequestBody GuestDetails guestDetails) {
		// Validate input

		if (guestDetails == null || guestDetails.getCheckInDate() == null) {
			return ResponseEntity.badRequest().body("GuestDetails checkindate must not be null or empty");
		}

		if (guestDetails == null || guestDetails.getCheckInTime() == null) {
			return ResponseEntity.badRequest().body("GuestDetails checkintime must not be null or empty");
		}

		if (guestDetails == null || guestDetails.getCheckOutTime() == null) {
			return ResponseEntity.badRequest().body("GuestDetails checkouttime must not be null or empty");
		}

		if (guestDetails == null || guestDetails.getCheckOutDate() == null) {
			return ResponseEntity.badRequest().body("GuestDetails checkoutdate must not be null or empty");
		}
		try {
			GuestDetails savedGuestDetails = service.createGuestDetail(guestDetails);
			return ResponseEntity.ok(savedGuestDetails);
		} catch (Exception e) {
			logger.error("Exception in controller of createguestdetail api :" + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the RoomType");
		}
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'GUESTDETAILS_UPDATE')")
//	@PreAuthorize("hasAuthority('GUESTDETAILS_UPDATE')")
	@PutMapping("/admin/updateguestdetail/{id}")
	public ResponseEntity<?> updateGuestDetails(@PathVariable Long id, @RequestBody GuestDetails guestDetails,
			HttpSession session) {
		// Validate input

		try {
			// Find existing RoomType
			GuestDetails existingGuestDetails = service.getGuestDetail(id);
			if (existingGuestDetails == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("GuestDetails with ID " + id + " not found");
			}
			session.setAttribute("oldValue", AuditUtil.toJson(existingGuestDetails));
			// Update fields
			existingGuestDetails.setCheckInDate(guestDetails.getCheckInDate());

			existingGuestDetails.setCheckInTime(guestDetails.getCheckInTime());

			existingGuestDetails.setCheckOutDate(guestDetails.getCheckOutDate());

			existingGuestDetails.setCheckOutTime(guestDetails.getCheckOutTime());
//			existingGuestDetails.setDocumentDetails(guestDetails.getDocumentDetails());
			existingGuestDetails.setDocumentDetailsId(guestDetails.getDocumentDetailsId());
			existingGuestDetails.setGuestDetailsStatus(guestDetails.getGuestDetailsStatus());

//			existingGuestDetails.setPersonalDetails(guestDetails.getPersonalDetails());
			existingGuestDetails.setPersonalDetailsId(guestDetails.getPersonalDetailsId());
//			existingGuestDetails.setRentDetails(guestDetails.getRentDetails());
			existingGuestDetails.setRentDetailsId(guestDetails.getRentDetailsId());
			existingGuestDetails.setIsDeleted(guestDetails.getIsDeleted());
			existingGuestDetails.setNoOfDays(guestDetails.getNoOfDays());
			existingGuestDetails.setRoomMasterId(guestDetails.getRoomMasterId());
			existingGuestDetails.setStayDetailsId(guestDetails.getStayDetailsId());
			existingGuestDetails.setRoomMaster(guestDetails.getRoomMaster());
			existingGuestDetails.setNoOfDays(guestDetails.getNoOfDays());
			// Save updated RoomType
			GuestDetails updatedGuestDetails = service.updateGuestDetails(existingGuestDetails.getId(),
					existingGuestDetails);

			return ResponseEntity.ok(updatedGuestDetails);

		} catch (Exception e) {
			logger.error("Exception in controller of updateGuestDetails api :" + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the RoomType");
		}
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'GUESTDETAILS_DELETE')")
//	@PreAuthorize("hasAuthority('GUESTDETAILS_DELETE')")
	@DeleteMapping("/admin/deleteguestdetail/{id}")
	public ResponseEntity<String> deleteGuestDetail(@PathVariable("id") int id) {
		boolean isDeleted = service.deleteSoftStayDetails(id);
		if (isDeleted) {
			String responseContent = "GuestDetails has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting RentDetails from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@PreAuthorize("hasAuthority('GUESTDETAILS_VIEW')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'GUESTDETAILS_VIEW')")
	@GetMapping("/room-activity")
	public ResponseEntity<List<RoomActivityResponseDTO>> getRoomActivities(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,

			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

		return ResponseEntity.ok(service.getRoomActivities(fromDate, toDate));

	}

	@Operation(summary = "Update Guest Current Status")
//	@PreAuthorize("hasAuthority('GUESTDETAILS_UPDATE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'GUESTDETAILS_UPDATE')")
	@PostMapping("/update-guest-status")
	public ResponseEntity<String> updateGuestStatus(@RequestParam Long guestDetailsId,@RequestParam String newStatus) {

		service.updateGuestStatus(guestDetailsId, newStatus);

		return ResponseEntity.ok("Guest status updated successfully");
	}
	
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'GUESTDETAILS_VIEW')")
//	@PreAuthorize("hasAuthority('GUESTDETAILS_VIEW')")
	@GetMapping(value = "/occupied-guest/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> getOccupiedGuestDetailsId(
            @PathVariable Long roomId) {

        return ResponseEntity.ok(
                service.getOccupiedGuestDetailsId(roomId));
    }
}
