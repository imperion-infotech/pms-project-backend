/**
 * 
 */
package com.pms.building.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pms.building.entity.Building;
import com.pms.common.repository.SoftDeleteRepository;

/**
 * 
 */
@Repository
public interface BuildingsRepository extends SoftDeleteRepository<Building, Long> , JpaSpecificationExecutor<Building>{
	
	List<Building> findByHotelId(Long hotelId);
	
	Building findByIdAndHotelId(Long buildingId,Long hotelId);
	
	List<Building> findByIsDeletedFalseAndHotelId(Long hotelId);
	List<Building> findByIsDeletedFalse();
	
	@Query("SELECT b FROM Building b WHERE b.hotelId = :hotelId and b.isDeleted=:isDeleted and b.isActive=:isActive")
	List<Building> findBuildings(@Param("hotelId") Long hotelId,@Param("isDeleted") Boolean isDeleted, @Param("isActive") Boolean isActive);

	
	
}
