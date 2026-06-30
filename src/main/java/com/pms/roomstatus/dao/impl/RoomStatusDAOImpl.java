/**
 * 
 */
package com.pms.roomstatus.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pms.roomstatus.dao.IRoomStatusDAO;
import com.pms.roomstatus.entity.RoomStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * 
 */
@Transactional
@Repository
public class RoomStatusDAOImpl implements IRoomStatusDAO  {
	
	
static final Logger logger = LoggerFactory.getLogger(RoomStatusDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<RoomStatus> getRoomStatuses() {
		String hql = "FROM RoomStatus as atcl ORDER BY atcl.id";
		return (List<RoomStatus>) entityManager.createQuery(hql).getResultList();
	}
	

	public RoomStatus getRoomStatus(Long roomStatusId) {
		return entityManager.find(RoomStatus.class, roomStatusId);
	}

	public RoomStatus createRoomStatus(RoomStatus roomStatus) {
		entityManager.persist(roomStatus);
		RoomStatus b = getLastInsertedRoomStatus();
		return b;
	}

	public RoomStatus updateRoomStatus(Long roomStatusId, RoomStatus roomStatus) {
		//First We are taking Book detail from database by given book id and 
				// then updating detail with provided book object
		RoomStatus roomStatusDB = getRoomStatus(roomStatusId);
		roomStatusDB.setRoomStatusName(roomStatus.getRoomStatusName());
		roomStatusDB.setRoomStatusTitle(roomStatus.getRoomStatusTitle());
		roomStatusDB.setRoomStatusTextColor(roomStatus.getRoomStatusTextColor());
		roomStatusDB.setRoomStatusColor(roomStatus.getRoomStatusColor());
		entityManager.flush();
				//again i am taking updated result of book and returning the book object
		RoomStatus updatedRoomStatus = getRoomStatus(roomStatusId);
				
				return updatedRoomStatus;
	}
	

	public boolean deleteRoomStatus(Long roomStatusId) {
		RoomStatus roomStatus = getRoomStatus(roomStatusId);
		entityManager.remove(roomStatus);
		
		//we are checking here that whether entityManager contains earlier deleted book or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(roomStatus);
		if(status){
			return false;
		}
		return true;
	}
	
	/**
	 * This method will get the latest inserted record from the database and return the object of Book class
	 * @return book
	 */
	private RoomStatus getLastInsertedRoomStatus(){
		String hql = "from RoomStatus order by id DESC";
		Query query = entityManager.createQuery(hql);
		query.setMaxResults(1);
		RoomStatus roomStatus = (RoomStatus)query.getSingleResult();
		return roomStatus;
	}


//	@Override
//	public RoomStatus findById(Long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	
//	/**
//	 * This method will get the latest inserted record from the database and return the object of Book class
//	 * @return book
//	 */
//	private Floor RoomStatus(){
//		String hql = "from Floor order by id DESC";
//		Query query = entityManager.createQuery(hql);
//		query.setMaxResults(1);
//		Floor floor = (Floor)query.getSingleResult();
//		return floor;
//	}




}
