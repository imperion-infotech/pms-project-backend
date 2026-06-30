/**
 * 
 */
package com.pms.room.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.pms.exception.ResourceNotFoundException;
import com.pms.floor.services.IFloorService;
import com.pms.room.dto.BulkRoomCreateRequestDTO;
import com.pms.room.dto.BulkRoomCreateResponseDTO;
import com.pms.room.dto.RoomMasterResponseDTO;
import com.pms.room.entity.RoomMaster;
import com.pms.room.services.IRoomMasterService;
import com.pms.roomtype.dao.RoomTypeRepository;
import com.pms.security.configuration.HotelContext;

/**
 * 
 */
@RestController
public class RoomMasterController {

	private static final Logger logger = LoggerFactory.getLogger(RoomMasterController.class);

	@Autowired
	private IRoomMasterService service;

	@Autowired
	private RoomTypeRepository roomTypeRepository;

	@Autowired
	private IFloorService floorService;

//	@GetMapping("/user/getroommasters")
////	@GetMapping("/auth/getroommasters")
//	public ResponseEntity<List<RoomMaster>> getRoomMasters() {
//		List<RoomMaster> roomMaster = service.getRoomMasters();
//		return new ResponseEntity<List<RoomMaster>>(roomMaster, HttpStatus.OK);
//
//	}

	@GetMapping("/user/getroommasters")
//	@PreAuthorize("hasAuthority('ROOMMASTER_VIEW')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMMASTER_VIEW')")
	public Page<RoomMasterResponseDTO> getRoomMasters(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		// Validate sort direction
		Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
		return service.getRoomMasters(pageable);

	}

	@GetMapping("/user/getroommaster/{id}")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMMASTER_VIEW')")
//	@PreAuthorize("hasAuthority('ROOMMASTER_VIEW')")
	public ResponseEntity<RoomMasterResponseDTO> getRoomMaster(@PathVariable("id") Long id) {
		RoomMasterResponseDTO roomMasterDTO = service.getRoomMaster(id);
		return new ResponseEntity<RoomMasterResponseDTO>(roomMasterDTO, HttpStatus.OK);
	}

//	@PreAuthorize("hasAuthority('ROOMMASTER_CREATE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMMASTER_CREATE')")
	@PostMapping("/admin/createroommaster")
	public ResponseEntity<?> createRoomMaster(@RequestBody RoomMaster roomMaster) {
		// Validate input
		if (roomMaster == null || roomMaster.getRoomName() == null) {
			return ResponseEntity.badRequest().body("RoomMaster name must not be null or empty");
		}
		if (roomMaster == null || roomMaster.getRoomShortName() == null
				|| roomMaster.getRoomShortName().trim().isEmpty()) {

			return ResponseEntity.badRequest().body("RoomMaster color must not be null or empty");
		}

//		if (roomMaster == null || roomMaster.getFloor().getId() == 0)
//				{
//			return ResponseEntity.badRequest().body("RoomMaster floorid must not be null or empty");
//		}

		try {
			RoomMaster savedRoomMaster = service.createRoomMaster(roomMaster);

			return ResponseEntity.ok(savedRoomMaster);
		} catch (Exception e) {
			logger.error("Exception in controller of createRoomMaster api :" + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the roomMaster");
		}
	}

//	@PreAuthorize("hasAuthority('ROOMMASTER_UPDATE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMMASTER_UPDATE')")
	@PutMapping("/admin/updateroommaster/{id}")
	public ResponseEntity<?> updateRoomMaster(@PathVariable Long id, @RequestBody RoomMaster roomMaster) {
		if (roomMaster == null || roomMaster.getRoomName() == null) {
			return ResponseEntity.badRequest().body("RoomMaster name must not be null or empty");
		}

		if (roomMaster == null || roomMaster.getRoomShortName() == null
				|| roomMaster.getRoomShortName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("RoomMaster color must not be null or empty");
		}

//		if (roomMaster == null || roomMaster.getFloor().getId() == 0)
//		{
//			return ResponseEntity.badRequest().body("RoomMaster floorid must not be null or empty");
//		}
		try {
			// Find existing RoomType
			RoomMaster existingRoomMaster = service.getRoomMasterByIdAndHotelID(id);
			if (existingRoomMaster == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RoomMaster with ID " + id + " not found");
			}

			// Update fields
			existingRoomMaster.setFloor(roomMaster.getFloor());
			existingRoomMaster.setRoomName(roomMaster.getRoomName());
			existingRoomMaster.setRoomShortName(roomMaster.getRoomShortName());
//			existingRoomMaster.setRoomStatusTableId(roomMaster.getRoomStatusTableId());

			Long hotelId = HotelContext.getHotelId();
			if (hotelId == null) {
				throw new ResourceNotFoundException("Hotel not selected");
			}

			//Null check of roomtype for any room
			if(roomMaster.getRoomType()!= null)
			{
			existingRoomMaster.setRoomType(
					roomTypeRepository.findByIdAndHotelId((long) roomMaster.getRoomType().getId(), hotelId));
			}

			existingRoomMaster.setHandicap(roomMaster.getHandicap());
			existingRoomMaster.setSmoking(roomMaster.getSmoking());
			existingRoomMaster.setNonRoom(roomMaster.getNonRoom());
			existingRoomMaster.setNonSmoking(roomMaster.getNonSmoking());
			existingRoomMaster.setPet(roomMaster.getPet());
//			existingRoomMaster.setRoomStatusId(roomMaster.getRoomStatusId());
			existingRoomMaster.setRoomStatus(roomMaster.getRoomStatus());
			// You can add more setters here for other updatable fields

			// Save updated RoomType
			RoomMaster updatedRoomMaster = service.updateRoomMaster(existingRoomMaster.getId(), existingRoomMaster);

			return ResponseEntity.ok(updatedRoomMaster);

		} catch (Exception e) {
			logger.error("Exception in controller of updateroommaster api :" + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the roomMaster");
		}
	}

//	@PreAuthorize("hasAuthority('ROOMMASTER_DELETE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMMASTER_DELETE')")
	@DeleteMapping("/admin/deleteroommaster/{id}")
	public ResponseEntity<String> deleteRoomStatus(@PathVariable("id") Long id) {
		boolean isDeleted = service.deleteRoomMaster(id);
		if (isDeleted) {
			String responseContent = "RoomMaster has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting RoomMaster from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@PreAuthorize("hasAuthority('ROOMMASTER_SEARCH')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMMASTER_SEARCH')")
	@GetMapping("/user/roommaster/search")
	public ResponseEntity<List<RoomMasterResponseDTO>> search(@RequestParam(required = false) String roomName,
			@RequestParam(required = false) String roomShortName, @RequestParam(required = false) String floorName) {

		return ResponseEntity.ok(service.search(roomName, roomShortName, floorName));
	}

//	@PreAuthorize("hasAuthority('ROOMMASTER_CREATE')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMMASTER_CREATE')")
	@PostMapping("/admin/roommaster/bulk-create")
	public ResponseEntity<BulkRoomCreateResponseDTO> bulkCreateRooms(@RequestBody BulkRoomCreateRequestDTO requestDTO) {

		BulkRoomCreateResponseDTO response = service.bulkCreateRooms(requestDTO);

		return ResponseEntity.ok(response);
	}

//	@PreAuthorize("hasAuthority('ROOMMASTER_VIEW')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMMASTER_VIEW')")
	@GetMapping("/floor/{floorId}")
	public ResponseEntity<List<RoomMasterResponseDTO>> getRoomsByFloor(@PathVariable Long floorId) {

		List<RoomMasterResponseDTO> rooms = service.getRoomsByFloor(floorId);

		return ResponseEntity.ok(rooms);
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMMASTER_VIEW')") 
	@GetMapping("/by-room-type-and-room-status/{roomTypeId}/{roomStatusId}")
	public ResponseEntity<List<RoomMasterResponseDTO>> getRoomsByRoomTypeAndRoomStatus(@PathVariable Long roomTypeId,
			@PathVariable Long roomStatusId) {

		Long hotelId = HotelContext.getHotelId();

		return ResponseEntity.ok(service.getRoomsByRoomTypeAndRoomStatus(roomTypeId, roomStatusId, hotelId));
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'ROOMMASTER_UPDATE')")
	@PutMapping("/{roomId}/room-status/{roomStatusId}")
	public ResponseEntity<RoomMasterResponseDTO> updateRoomStatusOfRoomMaster(@PathVariable Long roomId,
			@PathVariable Long roomStatusId) {

		RoomMasterResponseDTO response = service.updateRoomStatusOfRoomMaster(roomId, roomStatusId);

		return ResponseEntity.ok(response);
	}

}
