package com.pms.floor.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pms.building.dao.BuildingsRepository;
import com.pms.building.entity.Building;
import com.pms.common.service.SoftDeleteService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.floor.dao.FloorsRepository;
import com.pms.floor.dao.IFloorDAO;
import com.pms.floor.dto.BulkFloorCreateRequestDTO;
import com.pms.floor.dto.BulkFloorCreateResponseDTO;
import com.pms.floor.entity.Floor;
import com.pms.floor.entity.FloorMapper;
import com.pms.floor.entity.FloorResponseDTO;
import com.pms.floor.services.IFloorService;
import com.pms.hotel.entity.Hotel;
import com.pms.hotel.repository.HotelRepository;
import com.pms.room.dao.impl.RoomMasterRepository;
import com.pms.room.entity.RoomMaster;
import com.pms.search.specification.FloorSpecification;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.AuthService;
import com.pms.security.service.BaseHotelService;

import jakarta.transaction.Transactional;

@Service
public class FloorServiceImpl extends BaseHotelService implements IFloorService {

	private static final Logger logger = LoggerFactory.getLogger(FloorServiceImpl.class);

	@Autowired
	private IFloorDAO dao;

	@Autowired
	private FloorsRepository floorsRepository;

	@Autowired
	private SoftDeleteService softDeleteService;

	@Autowired
	private AuthService authService;

	@Autowired
	private HotelRepository hotelRepository;
	@Autowired
	private BuildingsRepository buildingsRepository;
	@Autowired
	private RoomMasterRepository roomMasterRepository;

	public FloorServiceImpl(IFloorDAO dao, FloorsRepository floorsRepository) {
		super();
		this.dao = dao;
		this.floorsRepository = floorsRepository;
	}

	public List<FloorResponseDTO> getFloors() {

		Long hotelId = HotelContext.getHotelId();

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

		List<Floor> floors = floorsRepository.findByHotelId(hotelId);

		return floors.stream().map(FloorMapper::toDTO).toList();
	}

	public Floor createFloor(Floor floor) {
//		return dao.createFloor(Floor);
		assignHotel(floor, floor.getHotelId());
		floor.setCreatedBy(authService.getCurrentUser().getId());
		return floorsRepository.save(floor);
	}

	public Floor updateFloor(Long floorId, Floor floor) {
		validateHotelAccess(floor.getHotelId());
		Long userId = UserContext.getUserId();
		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		floor.setUpdatedBy(userId);
		floor.setUpdatedOn(LocalDateTime.now());
		Floor b = floorsRepository.save(floor);
//		return dao.updateFloor(floorId, floor);
		return b;
	}

	public Floor getFloor(Long floorId) {
		return floorsRepository.findByIdAndHotelId(floorId, HotelContext.getHotelId());
	}

	public boolean deleteFloor(Long floorId) {
//		Floor b = getFloor(floorId);
		
		Floor b = floorsRepository.findByIdAndHotelId(floorId, HotelContext.getHotelId());
		validateHotelAccess(b.getHotelId());
		
		boolean roomExists = roomMasterRepository.existsByFloorIdAndIsDeletedFalse(floorId);

	    if (roomExists) {
	        throw new IllegalStateException(
	                "Cannot delete floor because rooms are assigned to this floor");
	    }

		
		Floor c = softDeleteService.softDelete(floorId, floorsRepository);

		return c == null ? false : true;

	}

	@Override
	public List<Floor> search(String name, String description) {

		Long hotelId = HotelContext.getHotelId(); // 🔥 get from JWT

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

		Specification<Floor> spec = Specification.where(FloorSpecification.hasHotelId(hotelId)) // ✅ ADD THIS
				.and(FloorSpecification.hasName(name)).and(FloorSpecification.hasDescription(description));

		return floorsRepository.findAll(spec);
	}

	@Override
	public BulkFloorCreateResponseDTO bulkCreateFloors(BulkFloorCreateRequestDTO dto) {

		Hotel hotel = hotelRepository.findById(dto.getHotelId())
				.orElseThrow(() -> new RuntimeException("Hotel not found"));

		Building building = buildingsRepository.findById(dto.getBuildingId())
				.orElseThrow(() -> new RuntimeException("Building not found"));

		/*
		 * VALIDATE BUILDING BELONGS TO HOTEL
		 */
		if (!building.getHotel().getId().equals(hotel.getId())) {

			throw new RuntimeException("Building does not belong to hotel");
		}

		List<String> createdFloors = new ArrayList<>();

		List<String> duplicateFloors = new ArrayList<>();

		for (int i = 0; i < dto.getTotalFloors(); i++) {

			String floorName = "Floor " + (dto.getStartFloor() + i);

			/*
			 * CHECK DUPLICATE FLOOR
			 */
			boolean exists = floorsRepository.existsByNameAndHotelId(floorName, dto.getHotelId());

			/*
			 * SKIP DUPLICATE
			 */
			if (exists) {

				duplicateFloors.add(floorName);

				continue;
			}

			Floor floor = new Floor();

			floor.setName(floorName);

			floor.setHotel(hotel);

			floor.setBuilding(building);
			
			floor.setNoOfRooms(dto.getNoOfRooms());

			floorsRepository.save(floor);

			createdFloors.add(floorName);
		}

		/*
		 * RESPONSE
		 */
		BulkFloorCreateResponseDTO response = new BulkFloorCreateResponseDTO();

		response.setTotalCreated(createdFloors.size());

		response.setCreatedFloors(createdFloors);

		response.setDuplicateFloors(duplicateFloors);

		return response;
	}
	
	
	@Override
	public List<FloorResponseDTO> getFloorsByBuilding(Long buildingId) {

	    Long hotelId = HotelContext.getHotelId();

	    if (hotelId == null) {
	        throw new ResourceNotFoundException("Hotel not selected");
	    }

	    validateHotelAccess(hotelId);

	    Building building = buildingsRepository
	            .findByIdAndHotelId(buildingId, hotelId);

	    if (building == null) {
	        throw new ResourceNotFoundException("Building not found");
	    }

	    List<Floor> floors =
	    		floorsRepository.findByBuildingIdAndIsDeletedFalse(buildingId);

	    return floors.stream()
	            .map(this::mapToDTO)
	            .toList();
	}
	
	private FloorResponseDTO mapToDTO(Floor floor) {

	    FloorResponseDTO dto = new FloorResponseDTO();

	    dto.setId(floor.getId());
	    dto.setName(floor.getName());
	    dto.setDescription(floor.getDescription());

	   /* if (floor.getBuilding() != null) {

	        dto.setBuildingId(floor.getBuilding().getId());

	        dto.setBuildingName(
	                floor.getBuilding().getName());
	    }*/

	    return dto;
	}
	
	
	 @Transactional
	 public void deleteFloorHierarchy(Long floorId) {
		  
		  boolean roomExists = roomMasterRepository.existsByFloorIdAndIsDeletedFalse(floorId);

		    if (roomExists) {
		        throw new IllegalStateException(
		                "Cannot delete floor because rooms are assigned to this floor");
		    }

	         List<RoomMaster> rooms =
	                 roomMasterRepository.findByFloorIdAndIsDeletedFalse(floorId);

	         for (RoomMaster room : rooms) {
	        	 
	        	 softDeleteService.softDelete(room.getId(), roomMasterRepository);
//	             room.setIsDeleted(true);
	         }

//	         roomMasterRepository.saveAll(rooms);
	         softDeleteService.softDelete(floorId, floorsRepository);
	 }
	
	
	
}
