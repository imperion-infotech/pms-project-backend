/**
 * 
 */
package com.pms.guestdetails.service;

import java.time.LocalDateTime;
import java.util.List;

import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dto.RoomActivityResponseDTO;

/**
 * 
 */
public interface IGuestDetailsService {
	
	public List<GuestDetails> getGuestDetails();
	public GuestDetails getGuestDetail(Long guestDetailsId);
	public GuestDetails createGuestDetail(GuestDetails guestDetails);
	public GuestDetails updateGuestDetails(Long guestDetailsId,GuestDetails guestDetails);
	public boolean deleteSoftStayDetails(Integer id);
	List<RoomActivityResponseDTO> getRoomActivities(LocalDateTime fromDate,LocalDateTime toDate);
	void updateGuestStatus(Long guestDetailsId, String newStatus);
	Long getOccupiedGuestDetailsId(Long roomId);
	
	

}
