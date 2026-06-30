/**
 * 
 */
package com.pms.roomstatus.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.roomstatus.entity.RoomStatus;

/**
 * 
 */
public interface RoomStatusRepository extends SoftDeleteRepository<RoomStatus, Long> , JpaSpecificationExecutor<RoomStatus>{
	
List<RoomStatus> findByHotelId(Long hotelId);
	
	RoomStatus findByIdAndHotelId(Long roomStatusId,Long hotelId);
	
	List<RoomStatus> findByIsDeletedFalseAndHotelId(Long hotelId);
	List<RoomStatus> findByIsDeletedFalse();
	
//	@Query("SELECT f FROM RoomStatus f WHERE f.hotelId = :hotelId")
//	List<RoomStatus> findRoomStatus(@Param("hotelId") Long hotelId);
	
	@Query("SELECT r FROM RoomStatus r WHERE r.hotelId = :hotelId and r.isDeleted=:isDeleted and r.isActive=:isActive")
	List<RoomStatus> findRoomStatus(@Param("hotelId") Long hotelId,@Param("isDeleted") Boolean isDeleted, @Param("isActive") Boolean isActive);

	 RoomStatus findByRoomStatusName(String roomStatusName);
	 
	 RoomStatus findByRoomStatusNameAndHotelId(String roomStatusName,Long hotelId);
	 
	 Optional<RoomStatus> findByIdAndIsDeletedFalseAndHotelId(Long roomStatusId, Long hotelId);
	 

}

