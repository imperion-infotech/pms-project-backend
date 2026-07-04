/**
 * 
 */
package com.pms.guestdetails.dao.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.pms.guestdetails.dto.RoomActivityResponseDTO;

/**
 * 
 */
public interface GuestDetailsRepositoryCustom {
	
	 List<RoomActivityResponseDTO> getRoomActivities(Long hotelId,
	            LocalDateTime fromDate,
	            LocalDateTime toDate);

}
