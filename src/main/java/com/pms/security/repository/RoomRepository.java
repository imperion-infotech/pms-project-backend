/**
 * 
 */
package com.pms.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.room.entity.RoomMaster;

/**
 * 
 */
public interface RoomRepository extends JpaRepository<RoomMaster, Integer> {
	RoomMaster findFirstByRoomStatus_RoomStatusName(String roomStatusName);
}
