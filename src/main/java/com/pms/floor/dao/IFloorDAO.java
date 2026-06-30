package com.pms.floor.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pms.floor.entity.Floor;
@Repository
public interface IFloorDAO  {
	
	static final Logger logger = LoggerFactory.getLogger(IFloorDAO.class);
	
	public List<com.pms.floor.entity.Floor> getFloors();
	public Floor getFloor(Long floorId);
	public Floor createFloor(Floor floor);
	public Floor updateFloor(Long floorId,Floor floor);
	public boolean deleteFloor(Long floorId);
	public Floor findById(Long id);

}
