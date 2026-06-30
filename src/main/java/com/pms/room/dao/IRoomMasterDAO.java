/**
 * 
 */
package com.pms.room.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pms.room.entity.RoomMaster;

/**
 * 
 */
public interface IRoomMasterDAO {
	
static final Logger logger = LoggerFactory.getLogger(IRoomMasterDAO.class);
	
	public Page<RoomMaster> getRoomMasters(Pageable pageable);
	public RoomMaster getRoomMaster(Long roomMasterId);
	public RoomMaster createRoomMaster(RoomMaster roomStatus);
	public RoomMaster updateRoomMaster(Long roomMasterId,RoomMaster roomMaster);
	public boolean deleteRoomMaster(Long roomMasterId);
	public RoomMaster findById(Integer id);

}
