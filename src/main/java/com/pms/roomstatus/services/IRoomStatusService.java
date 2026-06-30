/**
 * 
 */
package com.pms.roomstatus.services;

import java.util.List;

import com.pms.roomstatus.entity.RoomStatus;

/**
 * 
 */
public interface IRoomStatusService {
	
	
	List<RoomStatus> getRoomStatuses();
	RoomStatus createRoomStatus(RoomStatus roomStatus);
	RoomStatus updateRoomStatus(Long roomTypeId, RoomStatus roomStatus);
	RoomStatus getRoomStatus(Long roomTypeId);
	boolean deleteRoomStatus(Long RoomTypeId);
	RoomStatus getRoomStatusById(Long id);
	public List<RoomStatus> search(String roomStatusName,String roomStatusDescription);

}
