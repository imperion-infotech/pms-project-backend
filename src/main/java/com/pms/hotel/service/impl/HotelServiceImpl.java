/**
 * 
 */
package com.pms.hotel.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pms.auditlog.annotation.Auditable;
import com.pms.auditlog.context.BusinessTraceContext;
import com.pms.auditlog.context.RequestTraceContext;
import com.pms.auditlog.entity.AuditLog;
import com.pms.auditlog.repository.AuditLogRepository;
import com.pms.auditlog.util.AuditUtil;
import com.pms.common.service.SoftDeleteService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.floor.dao.FloorsRepository;
import com.pms.hotel.entity.Hotel;
import com.pms.hotel.entity.HotelMapper;
import com.pms.hotel.entity.HotelRequestDTO;
import com.pms.hotel.entity.HotelResponseDTO;
import com.pms.hotel.entity.HotelUpdateRequestDTO;
import com.pms.hotel.entity.PropertyByIdDto;
import com.pms.hotel.repository.HotelRepository;
import com.pms.hotel.service.IHotelService;
import com.pms.nightaudit.entity.BusinessDate;
import com.pms.nightaudit.repository.BusinessDateRepository;
import com.pms.room.dao.impl.RoomMasterRepository;
import com.pms.search.specification.HotelSpecification;
import com.pms.security.configuration.HotelContext;
import com.pms.security.service.AuthService;
import com.pms.security.service.BaseHotelService;
import com.pms.security.util.SecurityUtils;
import com.pms.util.ConstantUtils;

import jakarta.transaction.Transactional;

/**
 * 
 */
@Service
public class HotelServiceImpl extends BaseHotelService implements IHotelService{
	
private	static final Logger logger = LoggerFactory.getLogger(HotelServiceImpl.class);
	
	@Autowired
	private HotelRepository hotelRepository;
	
	 @Autowired
     private SoftDeleteService softDeleteService;
	 
	 @Autowired
	private AuditLogRepository auditRepo;
	 
	 @Autowired
	 private RoomMasterRepository roomMasterRepository;
	 
	 @Autowired
	 private FloorsRepository floorsRepository;
	 
	 @Autowired
	 private BusinessDateRepository businessDateRepository;
	 
	 
	 @Autowired
	  private AuthService authService;
	 
	 @Override
	 public List<HotelResponseDTO> getHotels() {

		    logger.info("***** HotelServiceImpl.getHotels() called *****");

		    List<Hotel> hotels;

		    if (isSuperAdmin()) {

		        hotels = hotelRepository.findByIsDeletedFalse();

		    } else {

		        Long hotelId = HotelContext.getHotelId();

		        if (hotelId == null) {
		            throw new ResourceNotFoundException("Hotel not selected");
		        }

		        validateHotelAccess(hotelId);

		        Hotel hotel = hotelRepository.findById(hotelId)
		                .orElseThrow(() ->
		                        new ResourceNotFoundException("Hotel not found with id: " + hotelId));

		        hotels = Collections.singletonList(hotel);
		    }

		    logger.info("Hotels found: {}", hotels.size());

		    return hotels.stream()
		            .map(HotelMapper::toDTO)
		            .toList();
		}

	@Auditable(action = "CREATE", entity = "CREATE_HOTEL")
	@Override
	public Hotel createHotel(HotelRequestDTO  req) {
		Hotel hotel = new Hotel();

	    hotel.setHotelName(req.getHotelName()); // map correctly
	    hotel.setAddress(req.getAddress());
	    hotel.setCity(req.getCity());
	    hotel.setState(req.getState());
	    hotel.setCountry(req.getCountry());
	    hotel.setZipCode(req.getZipCode());
	    hotel.setEmail(req.getEmail());
	    hotel.setContactNumber(req.getContactNumber());
	    hotel.setStatus(req.getStatus());
	    hotel.setTimezone(req.getTimezone());
	    hotel.setUrl(req.getUrl());
	    hotel.setHotelImage(req.getHotelImage());
	    hotel.setHotelLogo(req.getHotelLogo());
	    Hotel hotel_new = hotelRepository.save(hotel);
	    
	    
	    BusinessDate businessDate = new BusinessDate();
	    businessDate.setHotelId(hotel_new.getId());
	    businessDate.setBusinessDate(LocalDate.now());
	    businessDate.setAuditRunning(false);
	    businessDate.setCurrentBusinessDate(true);
	    businessDate.setCreatedBy(authService.getCurrentUser().getId());
	   

	    businessDateRepository.save(businessDate);
	    
	    return hotel_new;
	    
	}

	@Override
	public HotelResponseDTO getHotel(Long hotelId) {

	    Hotel hotel = hotelRepository.findById(hotelId)
	            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

	    return HotelMapper.toDTO(hotel);
	}
	
	@Auditable(action = "UPDATE", entity = "UPDATE_HOTEL")
	public Hotel updateHotel(Long id, HotelUpdateRequestDTO request) {

	    Hotel existingHotel = hotelRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

	    // Audit can be done here (best place, not controller)
	    String oldValue = AuditUtil.toJson(existingHotel);

	    existingHotel.setAddress(request.getAddress());
	    existingHotel.setCity(request.getCity());
	    existingHotel.setContactNumber(request.getContactNumber());
	    existingHotel.setCountry(request.getCountry());
	    existingHotel.setEmail(request.getEmail());
	    existingHotel.setHotelImage(request.getHotelImage());
	    existingHotel.setHotelLogo(request.getHotelLogo());
	    existingHotel.setState(request.getState());
	    existingHotel.setStatus(request.getStatus());
	    existingHotel.setTimezone(request.getTimezone());
	    existingHotel.setUrl(request.getUrl());
	    existingHotel.setZipCode(request.getZipCode());

	    return hotelRepository.save(existingHotel);
	}
	
	
		
	@Auditable(action = "DELETE", entity = "DELETE_HOTEL")
	@Transactional
	public String deleteHotel(Long hotelId) {

		HotelResponseDTO hotel = getHotel(hotelId);
	    Long currentUserId = SecurityUtils.getCurrentUserId();

	    Hotel oldHotel = new Hotel();
	    BeanUtils.copyProperties(hotel, oldHotel);

	    roomMasterRepository.softDeleteRoomsByHotelId(hotelId, SecurityUtils.getCurrentUserId());
	    floorsRepository.softDeleteFloorsByHotelId(hotelId, SecurityUtils.getCurrentUserId());
	    hotelRepository.softDeleteHotel(hotelId, SecurityUtils.getCurrentUserId());

	    Hotel newHotel = new Hotel();
	    BeanUtils.copyProperties(oldHotel, newHotel);
	    newHotel.setIsDeleted(true);
	    newHotel.setIsActive(false);
	    newHotel.setDeletedBy(ConstantUtils.SUPER_ADMIN_ID);
	    newHotel.setDeletedOn(LocalDateTime.now());

	    AuditLog log = new AuditLog();
	    log.setAction("DELETE");
	    log.setEntityName("DELETE_HOTEL");
	    log.setEntityId(hotelId.toString());
	    log.setUsername(SecurityUtils.getCurrentUsername());
	    log.setTimestamp(LocalDateTime.now());
	    log.setOldValue(AuditUtil.toJson(oldHotel));
	    log.setNewValue(AuditUtil.toJson(newHotel));
	    log.setBusinessTraceId(BusinessTraceContext.get());
	    log.setRequestTraceId(RequestTraceContext.get());
	    log.setHotelId(hotelId);
	    auditRepo.save(log);

	    return "Hotel deleted successfully";
	}
	
	@Override
	public Hotel getHotelById(Long id) {
		return hotelRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Hotel not found"));
	}

	 protected boolean isSuperAdmin() {
		    return authService.getCurrentUser().hasRole(ConstantUtils.SUPER_ADMIN);
		}
	
	@Override
	public List<Hotel> search(String hotelName, String url,String address,String city, String state, String country , String email ,String status) {
		
			logger.info("current user id :::" + authService.getCurrentRole()+" superadmin id:: "+isSuperAdmin());
			Long hotelId = HotelContext.getHotelId();
			
			
		if(! isSuperAdmin())
		{
			validateHotelAccess(hotelId);
		}
		Specification<Hotel> spec = Specification.where(HotelSpecification.hasHotelId(hotelId))
				.and(HotelSpecification.isNotDeleted()) // ✅ ADD THIS
				.and(HotelSpecification.hasHotelName(hotelName))
				.and(HotelSpecification.hasUrl(url))
				.and(HotelSpecification.hasAddress(address))
				.and(HotelSpecification.hasCity(city))
				.and(HotelSpecification.hasState(state))
				.and(HotelSpecification.hasCountry(country))
				.and(HotelSpecification.hasEmail(email))
				.and(HotelSpecification.hasStatus(status));
		return hotelRepository.findAll(spec);		
		
	}
	
	 public PropertyByIdDto getPropertyById(Long id) {
	        return hotelRepository.findPropertyById(id)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Hotel not found: " + id));
	    }
	
}
