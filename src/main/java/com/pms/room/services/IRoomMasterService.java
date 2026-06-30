/**
 * 
 */
package com.pms.room.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pms.room.dto.BulkRoomCreateRequestDTO;
import com.pms.room.dto.BulkRoomCreateResponseDTO;
import com.pms.room.dto.RoomMasterResponseDTO;
import com.pms.room.entity.RoomMaster;

/**
 * 
 */
public interface IRoomMasterService {
	
	Page<RoomMasterResponseDTO> getRoomMasters(Pageable pageable);
	RoomMaster createRoomMaster(RoomMaster roomMaster);
	RoomMaster updateRoomMaster(Long roomTypeId, RoomMaster roomMaster);
	RoomMasterResponseDTO getRoomMaster(Long roomMasterId);
	boolean deleteRoomMaster(Long roomMasterId);
	public RoomMaster getRoomMasterByIdAndHotelID(Long id);
//	List<RoomMaster> search(String roomName, String roomShortName, String floorName);
	BulkRoomCreateResponseDTO bulkCreateRooms(BulkRoomCreateRequestDTO dto);
    List<RoomMasterResponseDTO> search(String roomName,String roomShortName,String floorName);
    List<RoomMasterResponseDTO> getRoomsByFloor(Long floorId);
    List<RoomMasterResponseDTO> getRoomsByRoomTypeAndRoomStatus(Long roomTypeId,Long roomStatusId,Long hotelId);
    
    RoomMasterResponseDTO updateRoomStatusOfRoomMaster(Long roomMasterId,Long roomStatusId);

}
