/**
 * 
 */
package com.pms.roomtype.services;

import java.util.List;

import com.pms.floor.entity.Floor;
import com.pms.roomtype.entity.RoomType;

/**
 * 
 */


public interface IRoomTypeService {
	
	List<RoomType> getRoomTypes();
	RoomType createRoomType(RoomType roomType);
	RoomType updateRoomType(Long roomTypeId, RoomType roomType);
	RoomType getRoomType(Long roomTypeId);
	boolean deleteRoomType(Long RoomTypeId);
	RoomType getRoomTypeById(Long id);
	List<RoomType> search(String shortName, String roomTypeName);
	

}
