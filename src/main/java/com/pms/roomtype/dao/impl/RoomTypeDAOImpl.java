/**
 * 
 */
package com.pms.roomtype.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pms.roomtype.dao.IRoomTypeDAO;
import com.pms.roomtype.entity.RoomType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

/**
 * 
 */
@Transactional
@Repository
public class RoomTypeDAOImpl implements IRoomTypeDAO  {

	static final Logger logger = LoggerFactory.getLogger(RoomTypeDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<RoomType> getRoomTypes() {
		String hql = "FROM RoomType as atcl ORDER BY atcl.id";
		return (List<RoomType>) entityManager.createQuery(hql).getResultList();
	}

	public RoomType getRoomType(Long roomTypeId) {
		return entityManager.find(RoomType.class, roomTypeId);
	}

	public RoomType createRoomType(RoomType roomType) {
		entityManager.persist(roomType);
		RoomType b = getLastInsertedRoomType();
		return b;
	}

	public RoomType updateRoomType(Long roomTypeId, RoomType roomType) {
		// First We are taking Book detail from database by given book id and
		// then updating detail with provided book object
		RoomType roomTypeFromDB = getRoomType(roomTypeId);

		roomTypeFromDB.setRoomTypeName(roomType.getRoomTypeName());
		roomTypeFromDB.setShortName(roomType.getShortName());
		roomTypeFromDB.setRoomTypeName(roomType.getRoomTypeName());
		entityManager.flush();

		// again i am taking updated result of book and returning the book object
		RoomType updatedRoomType = getRoomType(roomTypeId);

		return updatedRoomType;
	}

	public boolean deleteRoomType(Long roomTypeId) {
		RoomType roomType = getRoomType(roomTypeId);
		entityManager.remove(roomType);

		// we are checking here that whether entityManager contains earlier deleted book
		// or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(roomType);
		if (status) {
			return false;
		}
		return true;
	}

	/**
	 * This method will get the latest inserted record from the database and return
	 * the object of Book class
	 * 
	 * @return book
	 */
	private RoomType getLastInsertedRoomType() {
		String hql = "from RoomType order by id DESC";
		Query query = entityManager.createQuery(hql);
		query.setMaxResults(1);
		RoomType roomType = (RoomType) query.getSingleResult();
		return roomType;
	}

	public RoomType findById(Integer id) {
		return entityManager.find(RoomType.class, id);
	}


}
