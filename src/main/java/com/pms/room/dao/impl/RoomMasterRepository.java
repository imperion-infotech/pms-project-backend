/**
 * 
 */
package com.pms.room.dao.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.room.dto.RoomMasterResponseDTO;
import com.pms.room.entity.RoomMaster;

/**
 * 
 */
public interface RoomMasterRepository extends SoftDeleteRepository<RoomMaster, Long>, JpaSpecificationExecutor<RoomMaster>{

	Page<RoomMaster> findByHotelId(Long hotelId, Pageable pageable);
	
	RoomMaster findByIdAndHotelId(Long roomMasterId,Long hotelId);
	
	Page<RoomMaster> findByIsDeletedFalseAndHotelId(Long hotelId, Pageable pageable);
	Page<RoomMaster> findByIsDeletedFalse(Pageable pageable);
	List<RoomMasterResponseDTO> findByIsDeletedFalse();
	
	@Query("SELECT r FROM RoomMaster r WHERE r.hotelId = :hotelId and r.isDeleted=:isDeleted and r.isActive=:isActive")
	List<RoomMaster> findFloors(@Param("hotelId") Long hotelId,@Param("isDeleted") Boolean isDeleted, @Param("isActive") Boolean isActive);
	
	
	@Modifying
	@Query("""
	UPDATE RoomMaster r
	SET r.isDeleted = true,
	    r.isActive=false,
	    r.deletedOn = CURRENT_TIMESTAMP,
	    r.deletedBy = :deletedBy
	WHERE r.hotelId = :hotelId
	""")
	void softDeleteRoomsByHotelId(Long hotelId, Long deletedBy);
	
	boolean existsByRoomNameAndHotelId(
		    String roomName,
		    Long hotelId
		);
	
	 RoomMaster findFirstByRoomStatus_RoomStatusName(String roomStatusName);
	 
	 List<RoomMaster> findByFloorIdAndIsDeletedFalse(Long floorId);
	 
	 RoomMaster findByIdAndIsDeletedFalseAndHotelId(Long roomMasterId,Long hotelId);
	 
	 List<RoomMaster> findByRoomTypeIdAndRoomStatusIdAndIsDeletedFalseAndHotelId(Long roomTypeId,Long roomStatusId,Long hotelId);
	 
	 RoomMaster findByIdAndHotelIdAndIsDeletedFalse(Long roomMasterId,Long hotelId);
	 
	 boolean existsByFloorIdAndIsDeletedFalse(Long floorId);
	 
}
