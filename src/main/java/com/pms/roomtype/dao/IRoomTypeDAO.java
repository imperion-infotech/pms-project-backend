/**
 * 
 */
package com.pms.roomtype.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.floor.entity.Floor;
import com.pms.roomtype.entity.RoomType;

/**
 * 
 */
public interface IRoomTypeDAO {
	
static final Logger logger = LoggerFactory.getLogger(IRoomTypeDAO.class);
	
	public List<RoomType> getRoomTypes();
	public RoomType getRoomType(Long roomTypeId);
	public RoomType createRoomType(RoomType roomType);
	public RoomType updateRoomType(Long roomTypeId,RoomType floor);
	public boolean deleteRoomType(Long roomTypeId);
	public RoomType findById(Integer id);

}
