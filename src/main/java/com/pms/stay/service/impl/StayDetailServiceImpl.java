/**
 * 
 */
package com.pms.stay.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.auditlog.annotation.Auditable;
import com.pms.auditlog.context.BusinessTraceContext;
import com.pms.auditlog.context.RequestTraceContext;
import com.pms.auditlog.entity.AuditLog;
import com.pms.auditlog.repository.AuditLogRepository;
import com.pms.auditlog.util.AuditUtil;
import com.pms.common.service.SoftDeleteService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dao.impl.GuestDetailsRepository;
import com.pms.personaldetails.PersonalDetails;
import com.pms.personaldetails.PersonalDetailsRepository;
import com.pms.room.dao.impl.RoomMasterRepository;
import com.pms.room.entity.RoomMaster;
import com.pms.roomhistory.entity.RoomActivityHistory;
import com.pms.roomhistory.repository.RoomActivityHistoryRepository;
import com.pms.roomstatus.dao.RoomStatusRepository;
import com.pms.roomstatus.entity.RoomStatus;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.AuthService;
import com.pms.security.service.BaseHotelService;
import com.pms.security.util.SecurityUtils;
import com.pms.stay.dao.IStatyDetailsDAO;
import com.pms.stay.dao.StayDetailsRepository;
import com.pms.stay.dto.ChangeRoomRequestDTO;
import com.pms.stay.entity.StayDetails;
import com.pms.stay.service.IStayDetailsService;

import jakarta.transaction.Transactional;

/**
 * 
 */
@Service
public class StayDetailServiceImpl extends BaseHotelService implements IStayDetailsService {

	static final Logger logger = LoggerFactory.getLogger(StayDetailServiceImpl.class);

	@Autowired
	private IStatyDetailsDAO dao;
	private AuditLogRepository auditRepo;
	private StayDetailsRepository repository;
	@Autowired
	private SoftDeleteService softDeleteService;
	@Autowired
	private RoomMasterRepository roomMasterRepository;

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	@Autowired
	private RoomStatusRepository roomStatusRepository;

	@Autowired
	private RoomActivityHistoryRepository roomActivityHistoryRepository;

	@Autowired
	private AuthService authService;
	
	@Autowired
	private GuestDetailsRepository guestDetailsRepository;

	public StayDetailServiceImpl(IStatyDetailsDAO dao, AuditLogRepository auditRepo, StayDetailsRepository repository) {
		super();
		this.dao = dao;
		this.auditRepo = auditRepo;
		this.repository = repository;
	}

	public List<StayDetails> getStayDetails() {
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
		if (isSuperAdmin())
			return repository.findByIsDeletedFalse();
		else
			return repository.findByHotelIdAndIsDeletedFalse(HotelContext.getHotelId());

	}

	@Auditable(action = "CREATE", entity = "STAYDETAILS")
	public StayDetails createStayDetails(StayDetails stayDetails) {
		Long userId = UserContext.getUserId();

		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}

		PersonalDetails personal = personalDetailsRepository.findById(stayDetails.getPersonalDetailsId())
				.orElseThrow(() -> new RuntimeException("Personal details not found"));

		String businessTraceId = personal.getBusinessTraceId();
		BusinessTraceContext.set(businessTraceId);
		stayDetails.setBusinessTraceId(businessTraceId);

		assignHotel(stayDetails, stayDetails.getHotelId());
		stayDetails.setCreatedBy(userId);
		return repository.save(stayDetails);
	}

	@Auditable(action = "UPDATE", entity = "STAYDETAILS")
	public StayDetails updateStayDetails(Long stayDetailsId, StayDetails stayDetails) {

		validateHotelAccess(stayDetails.getHotelId());
		Long userId = UserContext.getUserId();
		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		BusinessTraceContext.set(stayDetails.getBusinessTraceId());
		stayDetails.setUpdatedBy(userId);
		stayDetails.setUpdatedOn(LocalDateTime.now());

		return repository.save(stayDetails);
	}

	public StayDetails getStayDetail(Long stayDetailsId) {
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
		return repository.findByIdAndHotelIdAndIsDeletedFalse(stayDetailsId, hotelId);
	}

	@Auditable(action = "DELETE", entity = "STAYDETAILS")
	@Transactional
	public boolean deleteSoftStayDetails(Long id) {

		Long hotelId = HotelContext.getHotelId();
		Long userId = UserContext.getUserId();
		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
		StayDetails entity = repository.findByIdAndHotelIdAndIsDeletedFalse(id, hotelId);
		BusinessTraceContext.set(entity.getBusinessTraceId());
		// ✅ Soft delete
//        entity.setDeleted(true);
//        entity.setDeletedOn(LocalDateTime.now());
//        entity.setDeletedBy(userId);
//        
//        repository.save(entity);

		StayDetails b = softDeleteService.softDelete(id, repository);

		// ✅ Audit log
		AuditLog log = new AuditLog();
		log.setAction("DELETE");
		log.setEntityName("STAY DETAILS");
		log.setEntityId(entity.getId().toString());
		log.setUsername(SecurityUtils.getCurrentUsername());
		log.setTimestamp(LocalDateTime.now());
		log.setOldValue(AuditUtil.toJson(entity));
		log.setBusinessTraceId(BusinessTraceContext.get());
		log.setRequestTraceId(RequestTraceContext.get());
		auditRepo.save(log);
		return b == null ? false : true;
	}

	
	@Override
	@Transactional
	public void changeRoom(ChangeRoomRequestDTO dto) {

		/*
		 * FETCH STAY DETAILS
		 */
		StayDetails stay = repository.findById(dto.getStayId())
				.orElseThrow(() -> new RuntimeException("Stay details not found"));
		
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);

		/*
		 * VALIDATE ACTIVE STAY
		 */
		if (Boolean.FALSE.equals(stay.getIsActive())) {
			throw new RuntimeException("Stay is not active");
		}

		if (Boolean.TRUE.equals(stay.getIsDeleted())) {
			throw new RuntimeException("Stay already deleted");
		}

		/*
		 * FETCH NEW ROOM
		 */
		RoomMaster newRoom = roomMasterRepository.findByIdAndHotelId(dto.getNewRoomId(), stay.getHotelId());

		if (newRoom == null) {
			new RuntimeException("New room not found");
		}

		/*
		 * CHECK ROOM AVAILABILITY
		 */

		RoomStatus availableStatus = roomStatusRepository.findByRoomStatusNameAndHotelId("Vacant Ready",
				stay.getHotelId());
		if (availableStatus == null) {
			new RuntimeException("AVAILABLE status not found");
		}

		RoomStatus occupiedStatus = roomStatusRepository.findByRoomStatusNameAndHotelId("Occupied Clean",
				stay.getHotelId());
		if (occupiedStatus == null) {
			new RuntimeException("OCCUPIED status not found");
		}

		if (!"Vacant Ready".equalsIgnoreCase(newRoom.getRoomStatus().getRoomStatusName())) {

			throw new RuntimeException("Selected room is not available");
		}

		/*
		 * OLD ROOM
		 */
		RoomMaster oldRoom = stay.getRoomMaster();

		/*
		 * UPDATE OLD ROOM STATUS
		 */
		oldRoom.setRoomStatus(availableStatus);
		/*
		 * UPDATE NEW ROOM STATUS
		 */
		newRoom.setRoomStatus(occupiedStatus);

		/*
		 * UPDATE STAY DETAILS
		 */
		stay.setRoomMaster(newRoom);
		stay.setBuilding(newRoom.getFloor().getBuilding());
		stay.setFloor(newRoom.getFloor());
		
		stay.setRoomType(newRoom.getRoomType());

		stay.setUpdatedOn(LocalDateTime.now());
		stay.setUpdatedBy(authService.getCurrentUser().getId());

		/*
		 * SAVE ROOMS
		 */
		roomMasterRepository.save(oldRoom);
		roomMasterRepository.save(newRoom);

		/*
		 * SAVE STAY
		 */
		repository.save(stay);

		/*
		 * OPTIONAL: SAVE ROOM CHANGE HISTORY
		 */

		/*
		 * SAVE ROOM CHANGE HISTORY
		 */
		RoomActivityHistory history = new RoomActivityHistory();

		history.setStayDetails(stay);

		history.setOldRoom(oldRoom);
		history.setNewRoom(newRoom);
		/*
		 * SNAPSHOT VALUES
		 */
		history.setOldRoomName(oldRoom.getRoomName());
		history.setNewRoomName(newRoom.getRoomName());

		/*
		 * HOTEL
		 */

		history.setHotelId(stay.getHotelId());

		/*
		 * REASON
		 */
		history.setReason(dto.getReason());

		history.setRemarks(dto.getRemarks());

		/*
		 * AUDIT
		 */
		history.setChangedBy(authService.getCurrentUser().getId());

		history.setChangedByName(authService.getCurrentUser().getUsername());

		history.setChangedOn(LocalDateTime.now());

		/*
		 * TRACE IDS
		 */
		PersonalDetails personal = personalDetailsRepository.findById(stay.getPersonalDetailsId())
				.orElseThrow(() -> new RuntimeException("Personal details not found"));

		String businessTraceId = personal.getBusinessTraceId();
		BusinessTraceContext.set(businessTraceId);

		history.setBusinessTraceId(businessTraceId);
		roomActivityHistoryRepository.save(history);

	}

}
