/**
 * 
 */
package com.pms.roomstatus.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pms.common.service.SoftDeleteService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.roomstatus.dao.IRoomStatusDAO;
import com.pms.roomstatus.dao.RoomStatusRepository;
import com.pms.roomstatus.entity.RoomStatus;
import com.pms.roomstatus.services.IRoomStatusService;
import com.pms.search.specification.RoomStatusSpecification;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.BaseHotelService;

/**
 * 
 */
@Service
public class RoomStatusServiceImpl extends BaseHotelService implements IRoomStatusService {
	
static final Logger logger = LoggerFactory.getLogger(RoomStatusServiceImpl.class);
	
	@Autowired
	private IRoomStatusDAO dao;
	
	@Autowired
	private RoomStatusRepository roomStatusRepository;
	
	@Autowired
	private SoftDeleteService softDeleteService;
	
	public RoomStatusServiceImpl(IRoomStatusDAO dao, RoomStatusRepository roomStatusRepository) {
		super();
		this.dao = dao;
		this.roomStatusRepository = roomStatusRepository;
	}

	public List<RoomStatus> getRoomStatuses() {
		Long hotelId = HotelContext.getHotelId();

	    if (hotelId == null) {
	        throw new ResourceNotFoundException("Hotel not selected");
	    }
	    validateHotelAccess(hotelId);
	    if (isSuperAdmin()) 
	    	return roomStatusRepository.findByIsDeletedFalse();
	    else 
	    	return roomStatusRepository.findByIsDeletedFalseAndHotelId(HotelContext.getHotelId());
	}

	public RoomStatus createRoomStatus(RoomStatus roomStatus) {
		Long userId = UserContext.getUserId();
		assignHotel(roomStatus, roomStatus.getHotelId());
	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
	    roomStatus.setCreatedBy(userId);
		 return roomStatusRepository.save(roomStatus);
	}

	public RoomStatus updateRoomStatus(Long roomStatusId, RoomStatus roomStatus) {
		Long userId = UserContext.getUserId();
	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
	    validateHotelAccess(roomStatus.getHotelId());
	    
	    
	    RoomStatus roomStatusDB = getRoomStatus(roomStatusId);
		roomStatusDB.setRoomStatusName(roomStatus.getRoomStatusName());
		roomStatusDB.setRoomStatusTitle(roomStatus.getRoomStatusTitle());
		roomStatusDB.setRoomStatusTextColor(roomStatus.getRoomStatusTextColor());
		roomStatusDB.setRoomStatusColor(roomStatus.getRoomStatusColor());
		roomStatusDB.setUpdatedBy(userId);
		roomStatusDB.setUpdatedOn(LocalDateTime.now());
		return roomStatusRepository.save(roomStatusDB);
	    
	}

	public RoomStatus getRoomStatus(Long roomStatusId) {
		Long hotelId = HotelContext.getHotelId();

	    if (hotelId == null) {
	        throw new ResourceNotFoundException("Hotel not selected");
	    }
	    validateHotelAccess(hotelId);
		return roomStatusRepository.findByIdAndHotelId(roomStatusId,HotelContext.getHotelId());
	}

	public boolean deleteRoomStatus(Long roomStatusId) {
		RoomStatus b = getRoomStatusById(roomStatusId);
		validateHotelAccess(b.getHotelId());
		RoomStatus c = softDeleteService.softDelete(roomStatusId, roomStatusRepository);
		return c == null ?false : true;
	}

	
	@Override
	public RoomStatus getRoomStatusById(Long id) {
		
		 return roomStatusRepository.findByIdAndHotelId(id,HotelContext.getHotelId());
	} 

	@Override
	public List<RoomStatus> search(String roomStatusName, String roomStatusDescription) {
		
		 Long hotelId = HotelContext.getHotelId();   // 🔥 get from JWT

	     if (hotelId == null) {
	         throw new ResourceNotFoundException("Hotel not selected");
	     }
	     
		Specification<RoomStatus> spec = Specification
				.where(RoomStatusSpecification.hasHotelId(hotelId))   
                .and(RoomStatusSpecification.hasName(roomStatusName))
                .and(RoomStatusSpecification.hasDescription(roomStatusDescription));

        return roomStatusRepository.findAll(spec);
	}
	
}
