/**
 * 
 */
package com.pms.guestdetails.service.impl;

import com.pms.security.service.AuthService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
import com.pms.guestdetails.dao.IGuestDetailsDAO;
import com.pms.guestdetails.dao.impl.GuestDetailsRepository;
import com.pms.guestdetails.dto.RoomActivityResponseDTO;
import com.pms.guestdetails.service.IGuestDetailsService;
import com.pms.paymentdetails.entity.PaymentDetails;
import com.pms.paymentdetails.repository.PaymentDetailsRepository;
import com.pms.personaldetails.PersonalDetails;
import com.pms.personaldetails.PersonalDetailsRepository;
import com.pms.room.entity.RoomMaster;
import com.pms.roomhistory.entity.RoomActivityHistory;
import com.pms.roomhistory.repository.RoomActivityHistoryRepository;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.BaseHotelService;
import com.pms.security.util.SecurityUtils;

import jakarta.transaction.Transactional;

/**
 * 
 */
@Service
public class GuestDetailsServiceImpl extends BaseHotelService implements IGuestDetailsService {

	private final AuthService authService;

	@Autowired
	private IGuestDetailsDAO dao;

	@Autowired
	private GuestDetailsRepository guestDetailsRepository;

	@Autowired
	private PaymentDetailsRepository paymentDetailsRepository;

	@Autowired
	private AuditLogRepository auditLogRepository;

	@Autowired
	private SoftDeleteService softDeleteService;

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
	private RoomActivityHistoryRepository roomActivityHistoryRepository;

	public GuestDetailsServiceImpl(IGuestDetailsDAO dao, GuestDetailsRepository guestDetailsRepository, AuthService authService) {
		super();
		this.dao = dao;
		this.guestDetailsRepository = guestDetailsRepository;
		this.authService = authService;
	}

	@Override
	public List<GuestDetails> getGuestDetails() {
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
		if (isSuperAdmin())
			return guestDetailsRepository.findByIsDeletedFalse();
		else
			return guestDetailsRepository.findByHotelIdAndIsDeletedFalse(HotelContext.getHotelId());
	}

	@Override
	public GuestDetails getGuestDetail(Long guestDetailsId) {
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
		return guestDetailsRepository.findByIdAndHotelIdAndIsDeletedFalse(Long.valueOf(guestDetailsId), hotelId);

	}

	@Auditable(action = "CREATE", entity = "GUESTDETAILS")
	public GuestDetails createGuestDetail(GuestDetails guestDetails) {
		Long userId = UserContext.getUserId();
		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}

		PersonalDetails personal = personalDetailsRepository.findById((long) guestDetails.getPersonalDetailsId())
				.orElseThrow(() -> new RuntimeException("Personal details not found"));
		String businessTraceId = personal.getBusinessTraceId();
		BusinessTraceContext.set(businessTraceId);
		guestDetails.setBusinessTraceId(businessTraceId);

		assignHotel(guestDetails, guestDetails.getHotelId());
		guestDetails.setCreatedBy(userId);

		return guestDetailsRepository.save(guestDetails);

	}

	@Auditable(action = "UPDATE", entity = "GUESTDETAILS")
	public GuestDetails updateGuestDetails(Long guestDetailsId, GuestDetails guestDetails) {
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
		Long userId = UserContext.getUserId();
		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		BusinessTraceContext.set(guestDetails.getBusinessTraceId());
		guestDetails.setUpdatedBy(userId);
		guestDetails.setUpdatedOn(LocalDateTime.now());
		return dao.updateGuestDetails(guestDetailsId, guestDetails);
	}

	@Auditable(action = "DELETE", entity = "GUESTDETAILS")
	@Transactional
	public boolean deleteSoftStayDetails(Integer id) {

		Long hotelId = HotelContext.getHotelId();
		Long userId = UserContext.getUserId();
		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
		GuestDetails entity = guestDetailsRepository.findByIdAndHotelIdAndIsDeletedFalse(Long.valueOf(id), hotelId);
//		BusinessTraceContext.set(entity.getBusinessTraceId());
		List<PaymentDetails> paymentDetails = paymentDetailsRepository.findByHotelId(hotelId);
		entity.setPaymentDetails(paymentDetails);
		GuestDetails b = softDeleteService.softDelete((long) id, guestDetailsRepository);

		// ✅ Audit log
		AuditLog log = new AuditLog();
		log.setAction("DELETE");
		log.setEntityName("GUEST DETAILS");
		log.setEntityId(entity.getId() + "");
		log.setUsername(SecurityUtils.getCurrentUsername());
		log.setTimestamp(LocalDateTime.now());
		log.setOldValue(AuditUtil.toJson(entity));
		log.setBusinessTraceId(BusinessTraceContext.get());
		log.setRequestTraceId(RequestTraceContext.get());
		auditLogRepository.save(log);
		return b == null ? false : true;
	}

	@Override
	public List<RoomActivityResponseDTO> getRoomActivities(LocalDateTime fromDate, LocalDateTime toDate) {
		
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);

		List<RoomActivityResponseDTO> responseList = guestDetailsRepository.getRoomActivities(fromDate, toDate);

		/*
		 * SET ACTIVITY TYPE
		 */
		for (RoomActivityResponseDTO dto : responseList) {

			/*
			 * CHECK-IN
			 */
			if (dto.getCheckInDateTime() != null && dto.getCheckOutDateTime() == null) {

				dto.setGuestDetailsStatus("CHECK_IN");
			}

			/*
			 * CHECK-OUT
			 */
			else if (dto.getCheckOutDateTime() != null) {

				dto.setGuestDetailsStatus("CHECK_OUT");
			}

			/*
			 * RESERVATION
			 */
			else if ("RESERVATION".equalsIgnoreCase(dto.getGuestDetailsStatus())) {

				dto.setGuestDetailsStatus("RESERVATION");
			}

			/*
			 * CHANGE ROOM
			 */
			else if ("CHANGE_ROOM".equalsIgnoreCase(dto.getGuestDetailsStatus())) {

				dto.setGuestDetailsStatus("CHANGE_ROOM");
			}
		}

		return responseList;
	}
	
	public void updateGuestStatus(Long guestDetailsId, String newStatus) {
		
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);

		GuestDetails guest = guestDetailsRepository.findById(guestDetailsId)
				.orElseThrow(() -> new RuntimeException("Guest not found"));

		/*
		 * OLD STATUS
		 */
		String oldStatus = guest.getGuestDetailsStatus();

		/*
		 * UPDATE CURRENT STATUS
		 */
		guest.setGuestDetailsStatus(newStatus);

		guestDetailsRepository.save(guest);

		/*
		 * INSERT HISTORY
		 */
		RoomActivityHistory history = new RoomActivityHistory();
		
		history.setHotelId(guest.getHotelId());

		history.setGuestDetailsId(guest.getId());

		history.setRoomMasterId(guest.getRoomMasterId());

		history.setOldStatus(oldStatus);

		history.setNewStatus(newStatus);

		history.setActivityType(newStatus);

		history.setChangedOn(LocalDateTime.now());

		history.setChangedBy(UserContext.getUserId());
		
		history.setChangedByName(authService.getCurrentUser().getUsername());
		
		history.setActivityDateTime(LocalDateTime.now());

		roomActivityHistoryRepository.save(history);
	}
	
	public Long getOccupiedGuestDetailsId(Long roomId) {
		
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

        Long guest_details_id =  guestDetailsRepository
                .findOccupiedGuestDetailsIdByRoomId(roomId,hotelId);
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "No occupied guest found for room id: " + roomId));
        return guest_details_id == null ? 0 : guest_details_id;
        
    }

}
