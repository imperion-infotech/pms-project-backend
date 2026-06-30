/**
 * 
 */
package com.pms.floor.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.building.entity.Building;
import com.pms.common.repository.SoftDeleteRepository;
import com.pms.floor.entity.Floor;

/**
 * 
 */

public interface FloorsRepository extends SoftDeleteRepository<Floor, Long> , JpaSpecificationExecutor<Floor>{
	
	List<Floor> findByHotelId(Long hotelId);
	
	Floor findByIdAndHotelId(Long floorId,Long hotelId);
	
	List<Floor> findByIsDeletedFalseAndHotelId(Long hotelId);
	List<Floor> findByIsDeletedFalse();
	
	@Query("SELECT f FROM Floor f WHERE f.hotelId = :hotelId and f.isDeleted=:isDeleted and f.isActive=:isActive")
	List<Floor> findFloors(@Param("hotelId") Long hotelId,@Param("isDeleted") Boolean isDeleted, @Param("isActive") Boolean isActive);
	
	@Modifying
	@Query("""
	UPDATE Floor f
	SET f.isDeleted = true,
		f.isActive= false,
	    f.deletedOn = CURRENT_TIMESTAMP,
	    f.deletedBy = :deletedBy
	WHERE f.hotel.id = :hotelId
	""")
	void softDeleteFloorsByHotelId(Long hotelId, Long deletedBy);
	boolean existsByNameAndHotelId(
	        String name,
	        Long hotelId
	);
	
	List<Floor> findByBuildingIdAndIsDeletedFalse(Long buildingId);
	
	 boolean existsByIdAndIsDeletedFalse(Long floorId);
	 
	 boolean existsByBuildingIdAndIsDeletedFalse(Long buildingId);

	  
	

}

