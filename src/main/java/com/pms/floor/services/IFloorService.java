package com.pms.floor.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.floor.dto.BulkFloorCreateRequestDTO;
import com.pms.floor.dto.BulkFloorCreateResponseDTO;
import com.pms.floor.entity.Floor;
import com.pms.floor.entity.FloorResponseDTO;
import com.pms.personaldetails.PersonalDetails;

public interface IFloorService  {
	
	static final Logger logger = LoggerFactory.getLogger(IFloorService.class);
	
	List<FloorResponseDTO> getFloors() ;
	Floor createFloor(Floor floor);
	Floor updateFloor(Long floorId, Floor floor);
	Floor getFloor(Long floorId);
	boolean deleteFloor(Long floorId);
//	Floor getFloorByIdAndHotelID(Integer id,Long hotelId);
	List<Floor> search(String name,String description);
	BulkFloorCreateResponseDTO bulkCreateFloors(
	         BulkFloorCreateRequestDTO dto);
	List<FloorResponseDTO> getFloorsByBuilding(Long buildingId);
	void deleteFloorHierarchy(Long floorId); 
	

}
