/**
 * 
 */
package com.pms.roomtype.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.building.entity.Building;
import com.pms.common.repository.SoftDeleteRepository;
import com.pms.roomtype.entity.RoomType;

/**
 * 
 */
public interface RoomTypeRepository  extends SoftDeleteRepository<RoomType, Long> , JpaSpecificationExecutor<RoomType>{
	List<RoomType> findByHotelId(Long hotelId);
	
	RoomType findByIdAndHotelId(Long roomTypeId,Long hotelId);
	
	RoomType findByIdAndIsDeletedFalseAndHotelId(Long roomTypeId,Long hotelId);
	
	List<RoomType> findByIsDeletedFalseAndHotelId(Long hotelId);
	List<RoomType> findByIsDeletedFalse();
	
	@Query("SELECT r FROM RoomType r WHERE r.hotelId = :hotelId and r.isDeleted=:isDeleted and r.isActive=:isActive")
	List<RoomType> findRoomTypes(@Param("hotelId") Long hotelId,@Param("isDeleted") Boolean isDeleted, @Param("isActive") Boolean isActive);
}
