package com.pms.floor.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pms.floor.dao.IFloorDAO;
import com.pms.floor.entity.Floor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Transactional
@Repository
public class FloorDAOImpl implements IFloorDAO {
	
	static final Logger logger = LoggerFactory.getLogger(FloorDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Floor> getFloors() {
		String hql = "FROM Floor as atcl ORDER BY atcl.id";
		return (List<Floor>) entityManager.createQuery(hql).getResultList();
	}
	

	public Floor getFloor(Long floorId) {
		return entityManager.find(Floor.class, floorId);
	}

	public Floor createFloor(Floor floor) {
		
		return floor;
	}

	public Floor updateFloor(Long floorId, Floor floor) {
		//First We are taking Book detail from database by given book id and 
				// then updating detail with provided book object
				Floor floorFromDB = getFloor(floorId);
				floorFromDB.setName(floor.getName());
				floorFromDB.setDescription(floor.getDescription());
				entityManager.flush();
				
				//again i am taking updated result of book and returning the book object
				Floor updatedFloor = getFloor(floorId);
				
				return updatedFloor;
	}

	public boolean deleteFloor(Long floorId) {
		Floor floor = getFloor(floorId);
		entityManager.remove(floor);
		
		//we are checking here that whether entityManager contains earlier deleted book or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(floor);
		if(status){
			return false;
		}
		return true;
	}


	@Override
	public Floor findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
	


