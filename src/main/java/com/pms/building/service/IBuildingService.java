/**
 * 
 */
package com.pms.building.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.building.entity.Building;

/**
 * 
 */
public interface IBuildingService {
	
static final Logger logger = LoggerFactory.getLogger(IBuildingService.class);
	
	List<Building> getBuildings();
	Building createBuilding(Building building);
	Building updateBuilding(Long buildingId, Building building);
	Building getBuilding(Long building);
	boolean deleteBuilding(Long building);
	Building getBuildingById(Long id);
	public List<Building> search(String name,String description);
	public void deleteBuildingHierarchy(Long buildingId);


}
