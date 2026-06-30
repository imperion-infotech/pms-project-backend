package com.pms.roommaster.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pms.building.service.IBuildingService;
import com.pms.common.service.SoftDeleteService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.floor.dao.FloorsRepository;
import com.pms.floor.entity.Floor;
import com.pms.floor.entity.RoomResponseDTO;
import com.pms.floor.services.IFloorService;
import com.pms.hotel.entity.Hotel;
import com.pms.hotel.repository.HotelRepository;
import com.pms.room.dao.IRoomMasterDAO;
import com.pms.room.dao.impl.RoomMasterRepository;
import com.pms.room.dto.BulkRoomCreateRequestDTO;
import com.pms.room.dto.BulkRoomCreateResponseDTO;
import com.pms.room.dto.RoomCreateDTO;
import com.pms.room.dto.RoomMasterResponseDTO;
import com.pms.room.entity.RoomMaster;
import com.pms.room.services.IRoomMasterService;
import com.pms.roomstatus.dao.RoomStatusRepository;
import com.pms.roomstatus.entity.RoomStatus;
import com.pms.roomtype.dao.RoomTypeRepository;
import com.pms.roomtype.entity.RoomType;
import com.pms.search.specification.RoomMasterSpecification;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.BaseHotelService;

import jakarta.transaction.Transactional;

@Service
public class RoomMasterServiceImpl extends BaseHotelService implements IRoomMasterService {

	public static final Logger logger = LoggerFactory.getLogger(RoomMasterServiceImpl.class);

	@Autowired
	private IRoomMasterDAO dao;

	@Autowired
	RoomMasterRepository roomMasterRepository;

	@Autowired
	RoomStatusRepository roomStatusRepository;

	@Autowired
	SoftDeleteService softDeleteService;

	@Autowired
	HotelRepository hotelRepository;

	@Autowired
	FloorsRepository floorsRepository;

	@Autowired
	RoomTypeRepository roomTypeRepository;

	@Autowired
	IBuildingService buildingService;

	@Autowired
	IFloorService floorService;

	public RoomMasterServiceImpl(IRoomMasterDAO dao, RoomMasterRepository roomMasterRepository,
			RoomStatusRepository roomStatusRepository) {
		super();
		this.dao = dao;
		this.roomMasterRepository = roomMasterRepository;
		this.roomStatusRepository = roomStatusRepository;
	}

	public Page<RoomMasterResponseDTO> getRoomMasters(Pageable pageable) {

		Long hotelId = HotelContext.getHotelId();

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

		validateHotelAccess(hotelId);

		Page<RoomMaster> page;

		if (isSuperAdmin()) {
			page = roomMasterRepository.findByIsDeletedFalseAndHotelId(hotelId, pageable);
		} else {
			page = roomMasterRepository.findByIsDeletedFalseAndHotelId(hotelId, pageable);
		}

		return page.map(this::convertToDTO);
	}

	private RoomMasterResponseDTO convertToDTO(RoomMaster room) {

		RoomMasterResponseDTO dto = new RoomMasterResponseDTO();

		dto.setId(room.getId());
		dto.setRoomName(room.getRoomName());
		dto.setRoomShortName(room.getRoomShortName());
		dto.setIsActive(room.getIsActive());
		dto.setHandicap(room.getHandicap());
		dto.setPet(room.getPet());
		dto.setNonRoom(room.getNonRoom());
		dto.setSmoking(room.getSmoking());
		dto.setNonSmoking(room.getNonSmoking());
		dto.setBuildingId(room.getBuildingId());

		if (room.getHotel() != null) {
			dto.setHotelId(room.getHotel().getId());
			dto.setHotelName(room.getHotel().getHotelName());
		}

		if (room.getFloor() != null) {
			dto.setFloorId(room.getFloor().getId());
			dto.setFloorName(room.getFloor().getName());

			// BUILDING
			if (room.getFloor().getBuilding() != null) {

				dto.setBuildingId(room.getFloor().getBuilding().getId());

//	            dto.setBuildingName(
//	                    room.getFloor().getBuilding().getName());
			}

		}

		/*if (room.getRoomType() == null) {
			throw new ResourceNotFoundException("RoomType not found");
		} else {
			
			dto.setRoomTypeId(room.getRoomType().getId());
			dto.setRoomTypeName(room.getRoomType().getRoomTypeName());
		}
*/
		if (room.getRoomStatus() != null) {
			dto.setRoomStatusId(room.getRoomStatus().getId());
			dto.setRoomStatusName(room.getRoomStatus().getRoomStatusName());
		}

		return dto;
	}

	public RoomMaster createRoomMaster(RoomMaster roomMaster) {
		Long userId = UserContext.getUserId();

		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		assignHotel(roomMaster, roomMaster.getHotelId());
		roomMaster.setCreatedBy(userId);
		return roomMasterRepository.save(roomMaster);
	}

	public RoomMaster updateRoomMaster(Long roomMasterId, RoomMaster roomMaster) {
		validateHotelAccess(roomMaster.getHotelId());
		Long userId = UserContext.getUserId();
		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		roomMaster.setUpdatedBy(userId);
		roomMaster.setUpdatedOn(LocalDateTime.now());
		RoomMaster b = roomMasterRepository.save(roomMaster);
		return b;
	}

	@Override
	public RoomMasterResponseDTO getRoomMaster(Long roomMasterId) {

		Long hotelId = HotelContext.getHotelId();

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

		validateHotelAccess(hotelId);

		RoomMaster roomMaster = roomMasterRepository.findByIdAndHotelId(roomMasterId, hotelId);

		if (roomMaster == null) {
			throw new ResourceNotFoundException("Room master not found");
		}

		return convertToDTO(roomMaster);
	}

	public boolean deleteRoomMaster(Long roomMasterId) {
		RoomMaster b = getRoomMasterByIdAndHotelID(roomMasterId);
		validateHotelAccess(b.getHotelId());
		RoomMaster c = softDeleteService.softDelete(roomMasterId, roomMasterRepository);

		return c == null ? false : true;
	}

	@Override
	public RoomMaster getRoomMasterByIdAndHotelID(Long id) { // ✅ Implemented method
		Long hotelId = HotelContext.getHotelId();
		return roomMasterRepository.findByIdAndHotelId(id, hotelId);
	}

	@Override
	public List<RoomMasterResponseDTO> search(String roomName, String roomShortName, String floorName) {

		Long hotelId = HotelContext.getHotelId();

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

		validateHotelAccess(hotelId);

		Specification<RoomMaster> spec = Specification.where(RoomMasterSpecification.hasHotelId(hotelId))
				.and(RoomMasterSpecification.hasRoomName(roomName))
				.and(RoomMasterSpecification.hasRoomShortName(roomShortName))
				.and(RoomMasterSpecification.hasFloorName(floorName));

		List<RoomMaster> rooms = roomMasterRepository.findAll(spec);

		return rooms.stream().map(this::convertToDTO).toList();
	}

	/*
	 * BULK CREATE
	 */
	@Override
	@Transactional
	public BulkRoomCreateResponseDTO bulkCreateRooms(BulkRoomCreateRequestDTO dto) {

		/*
		 * FETCH MASTER DATA
		 */
		Hotel hotel = hotelRepository.findById(dto.getHotelId())
				.orElseThrow(() -> new RuntimeException("Hotel not found"));

		Floor floor = floorsRepository.findById(dto.getFloorId())
				.orElseThrow(() -> new RuntimeException("Floor not found"));

		RoomType roomType = roomTypeRepository.findById(dto.getRoomTypeId())
				.orElseThrow(() -> new RuntimeException("Invalid RoomTypeId: " + dto.getRoomTypeId()));

		RoomStatus roomStatus = roomStatusRepository.findByRoomStatusName("Vacant Ready");

		if (roomStatus == null) {
			throw new RuntimeException("Room status 'Vacant Ready' not found");
		}

		/*
		 * VALIDATIONS
		 */
		validateFloorBelongsToHotel(floor, hotel);
		validateRoomTypeBelongsToHotel(roomType, hotel);

		/*
		 * BULK CREATE
		 */
		List<RoomMaster> savedRooms = new ArrayList<>();

		int totalRows = dto.getTotalRows();
		int roomsPerRow = dto.getRoomsPerRow();

		String prefix = (dto.getPrefix() == null) ? "" : dto.getPrefix();

		for (int row = 1; row <= totalRows; row++) {

			for (int room = 1; room <= roomsPerRow; room++) {

				String roomNumber = prefix + row + String.format("%02d", room);

				boolean exists = roomMasterRepository.existsByRoomNameAndHotelId(roomNumber, hotel.getId());

				if (exists) {
					throw new RuntimeException("Room already exists: " + roomNumber);
				}

				RoomMaster roomMaster = new RoomMaster();
				roomMaster.setHotel(hotel);
				roomMaster.setFloor(floor);
				roomMaster.setRoomType(roomType);
				roomMaster.setRoomStatus(roomStatus);
				roomMaster.setRoomName(roomNumber);
				roomMaster.setIsActive(true);
				roomMaster.setIsDeleted(false);
				roomMaster.setHandicap(false);
				roomMaster.setNonRoom(false);
				roomMaster.setNonSmoking(false);
				roomMaster.setPet(false);
				roomMaster.setSmoking(false);

				savedRooms.add(roomMasterRepository.save(roomMaster));
			}
		}

		/*
		 * RESPONSE
		 */
		BulkRoomCreateResponseDTO response = new BulkRoomCreateResponseDTO();
		response.setHotelId(hotel.getId());
		response.setHotelName(hotel.getHotelName());
		response.setFloorId(floor.getId());
		response.setFloorName(floor.getName());
		response.setRoomTypeId(roomType.getId());
		response.setRoomTypeName(roomType.getRoomTypeName());
		response.setTotalRoomsCreated(savedRooms.size());

		response.setRooms(savedRooms.stream().map(room -> {
			RoomResponseDTO dto1 = new RoomResponseDTO();
			dto1.setId(room.getId());
			dto1.setRoomName(room.getRoomName());
			dto1.setBuildingName(null != floor.getBuilding() ? floor.getBuilding().getName() : "");
			dto1.setRoomStatus(null != room.getRoomStatus() ? room.getRoomStatus().getRoomStatusName() : "");
			dto1.setRoomTypeName(null != room.getRoomType() ? room.getRoomType().getRoomTypeName() : "");
			return dto1;
		}).toList());

		return response;
	}

	/*
	 * PRIVATE VALIDATION METHODS
	 */
	private void validateFloorBelongsToHotel(Floor floor, Hotel hotel) {

		if (!floor.getHotel().getId().equals(hotel.getId())) {

			throw new RuntimeException("Floor does not belong to hotel");
		}
	}

	private void validateRoomTypeBelongsToHotel(RoomType roomType, Hotel hotel) {

		if (!roomType.getHotel().getId().equals(hotel.getId())) {

			throw new RuntimeException("Room type does not belong to hotel");
		}
	}

	private void validateRoomMasterRequest(BulkRoomCreateRequestDTO dto, Map<Long, RoomType> roomTypeMap,
			Map<Long, Floor> floorMap, Map<Long, RoomStatus> statusMap) {

		int index = 0;

		for (RoomCreateDTO room : dto.getRooms()) {
			index++;

			if (!roomTypeMap.containsKey(room.getRoomTypeId())) {
				throw new RuntimeException("Row " + index + ": Invalid RoomTypeId " + room.getRoomTypeId());
			}

			if (!floorMap.containsKey(room.getFloorId())) {
				throw new RuntimeException("Row " + index + ": Invalid FloorId " + room.getFloorId());
			}

			if (!statusMap.containsKey(room.getRoomStatusId())) {
				throw new RuntimeException("Row " + index + ": Invalid RoomStatusId " + room.getRoomStatusId());
			}
		}
	}

	@Override
	public List<RoomMasterResponseDTO> getRoomsByFloor(Long floorId) {

		Long hotelId = HotelContext.getHotelId();

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

		validateHotelAccess(hotelId);

		// Validate floor belongs to selected hotel
		Floor floor = floorsRepository.findByIdAndHotelId(floorId, hotelId);

		if (floor == null) {
			throw new ResourceNotFoundException("Floor not found");
		}

		List<RoomMaster> rooms = roomMasterRepository.findByFloorIdAndIsDeletedFalse(floorId);

		return rooms.stream().map(this::mapToDTO).toList();
	}

	private RoomMasterResponseDTO mapToDTO(RoomMaster room) {

		RoomMasterResponseDTO dto = new RoomMasterResponseDTO();

		dto.setId(room.getId());
		dto.setRoomName(room.getRoomName());
		dto.setRoomShortName(room.getRoomShortName());
		dto.setIsActive(room.getIsActive());
		dto.setIsDeleted(room.getIsDeleted());
		dto.setHandicap(room.getHandicap());
		dto.setNonRoom(room.getNonRoom());
		dto.setSmoking(room.getSmoking());
		dto.setNonSmoking(room.getNonSmoking());
		dto.setHotelId(room.getHotelId());
		dto.setHotelName(room.getHotel().getHotelName());
		if(room.getRoomStatus() != null) {
			dto.setRoomStatusId(room.getRoomStatus().getId());
			dto.setRoomStatusName(room.getRoomStatus().getRoomStatusName());
		}

		// Floor
		if (room.getFloor() != null) {

			dto.setFloorId(room.getFloor().getId());

			dto.setFloorName(room.getFloor().getName());

			// Building
			if (room.getFloor().getBuilding() != null) {

//				dto.setBuildingId(room.getFloor().getBuilding().getId());
				dto.setBuildingId(room.getBuildingId());

//				dto.setBuildingName(room.getFloor().getBuilding().getName());
			}
		}

		// Room Type
		if(room.getRoomType() != null)
		{
		RoomType newRoomType=roomTypeRepository.findByIdAndIsDeletedFalseAndHotelId(room.getRoomType().getId(),room.getHotelId());
		if (room.getRoomType() != null && newRoomType != null) {
			
			dto.setRoomTypeId(room.getRoomType().getId());

			dto.setRoomTypeName(room.getRoomType().getRoomTypeName());
		}
		}
		return dto;
	}

	public List<RoomMasterResponseDTO> getRoomsByRoomTypeAndRoomStatus(Long roomTypeId, Long roomStatusId,
			Long hotelId) {

		List<RoomMaster> rooms = roomMasterRepository
				.findByRoomTypeIdAndRoomStatusIdAndIsDeletedFalseAndHotelId(roomTypeId, roomStatusId, hotelId);

		return rooms.stream().map(room -> {

			RoomMasterResponseDTO dto = new RoomMasterResponseDTO();

			dto.setId(room.getId());
			dto.setRoomShortName(room.getRoomShortName());
			

			if(room.getRoomStatus() != null)
			{
				dto.setRoomStatusId(room.getRoomStatus().getId());
				dto.setRoomStatusName(room.getRoomStatus().getRoomStatusName());
				
			}
			
			if (room.getRoomType() != null) {
				dto.setRoomTypeId(room.getRoomType().getId());
				dto.setRoomTypeName(room.getRoomType().getRoomTypeName());
			}

			return dto;

		}).toList();
	}

	@Override
	@Transactional
	public RoomMasterResponseDTO updateRoomStatusOfRoomMaster(Long roomMasterId, Long roomStatusId) {

		Long hotelId = HotelContext.getHotelId();

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

		validateHotelAccess(hotelId);

		/*
		 * FETCH ROOM
		 */
		RoomMaster roomMaster = roomMasterRepository.findByIdAndHotelIdAndIsDeletedFalse(roomMasterId, hotelId);

		if (roomMaster == null) {

			throw new ResourceNotFoundException("Room not found");
		}

		/*
		 * FETCH ROOM STATUS
		 */
		RoomStatus roomStatus = roomStatusRepository.findByIdAndIsDeletedFalseAndHotelId(roomStatusId, hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Room status not found"));

		/*
		 * UPDATE STATUS
		 */
		roomMaster.setRoomStatus(roomStatus);

		roomMaster.setUpdatedBy(UserContext.getUserId());

		roomMaster.setUpdatedOn(LocalDateTime.now());

		RoomMaster updatedRoom = roomMasterRepository.save(roomMaster);

		/*
		 * RETURN DTO
		 */
		return convertToDTO(updatedRoom);
	}

}