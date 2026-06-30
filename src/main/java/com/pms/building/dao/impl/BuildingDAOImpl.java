/**
 * 
 */
package com.pms.building.dao.impl;

/**
 * 
 */
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pms.building.dao.IBuildingDAO;
import com.pms.building.entity.Building;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;




@Transactional
@Repository
public class BuildingDAOImpl implements IBuildingDAO {
	
	static final Logger logger = LoggerFactory.getLogger(BuildingDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Building> getBuildings() {
		String hql = "FROM Building as atcl ORDER BY atcl.id";
		return (List<Building>) entityManager.createQuery(hql).getResultList();
	}
	

	public Building getBuilding(Long buildingId) {
		return entityManager.find(Building.class, buildingId);
	}

	public Building createBuilding(Building building) {
		entityManager.persist(building);
		Building b = getLastInsertedBuilding();
		return b;
	}

	public Building updateBuilding(Long buildingId, Building building) {
		//First We are taking Book detail from database by given book id and 
				// then updating detail with provided book object
				Building buildingFromDB = getBuilding(buildingId);
				buildingFromDB.setName(building.getName());
				buildingFromDB.setDescription(building.getDescription());
				entityManager.flush();
				
				//again i am taking updated result of book and returning the book object
				Building updatedBuilding = getBuilding(buildingId);
				return updatedBuilding;
	}

	public boolean deleteBuilding(Long buildingId) {
		Building building = getBuilding(buildingId);
		entityManager.remove(building);
		
		//we are checking here that whether entityManager contains earlier deleted book or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(building);
		if(status){
			return false;
		}
		return true;
	}

	
	/**
	 * This method will get the latest inserted record from the database and return the object of Book class
	 * @return book
	 */
	private Building getLastInsertedBuilding(){
		String hql = "from Building order by id DESC";
		Query query = entityManager.createQuery(hql);
		query.setMaxResults(1);
		Building building = (Building)query.getSingleResult();
		return building;
	}


	@Override
	public Building findById(Integer id) {
		        return entityManager.find(Building.class, id);
		    }
	
	}
