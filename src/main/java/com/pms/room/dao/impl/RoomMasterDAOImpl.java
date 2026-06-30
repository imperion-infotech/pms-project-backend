/**
 * 
 */
package com.pms.room.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pms.floor.dao.IFloorDAO;
import com.pms.floor.entity.Floor;
import com.pms.room.dao.IRoomMasterDAO;
import com.pms.room.entity.RoomMaster;
import com.pms.roomstatus.dao.IRoomStatusDAO;
import com.pms.roomstatus.entity.RoomStatus;
import com.pms.roomtype.dao.IRoomTypeDAO;
import com.pms.roomtype.entity.RoomType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * 
 */

@Transactional
@Repository
public class RoomMasterDAOImpl implements IRoomMasterDAO {
	
	

static final Logger logger = LoggerFactory.getLogger(RoomMasterDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private IFloorDAO floorDao;
	
	@Autowired
	private IRoomTypeDAO roomTypeDAO;
	
	@Autowired
	private RoomMasterRepository roomMasterRepository;
	
	@Autowired
	private IRoomStatusDAO roomStatusDAO;
	
	

//	@SuppressWarnings("unchecked")
//	public List<RoomMaster> getRoomMasters() {
//		String hql = "FROM RoomMaster as atcl ORDER BY atcl.id";
//		return (List<RoomMaster>) entityManager.createQuery(hql).getResultList();
//	}
	
	@SuppressWarnings("unchecked")
	public Page<RoomMaster> getRoomMasters(Pageable pageable) {
		String hql = "FROM RoomMaster as atcl ORDER BY atcl.id";
		return  roomMasterRepository.findAll(pageable);
	}
	

	public RoomMaster getRoomMaster(Long roomMasterId) {
		
		RoomMaster rMaster= entityManager.find(RoomMaster.class, roomMasterId);
//		RoomStatus rStatus = roomStatusDAO.getRoomStatus(rMaster.getRoomStatusId());
//		rMaster.setRoomStatusTable(rStatus);
		return rMaster;
	}

	public RoomMaster createRoomMaster(RoomMaster roomMaster) {
		entityManager.persist(roomMaster);
//		RoomMaster b = getLastInsertedRoomMaster(roomMaster);
		return roomMaster;
	}

	public RoomMaster updateRoomMaster(Long roomMasterId, RoomMaster roomMaster) {
		//First We are taking Book detail from database by given book id and 
				// then updating detail with provided book object
		RoomMaster roomMasterDB = getRoomMaster(roomMasterId);
		roomMasterDB.setRoomName(roomMaster.getRoomName());
		roomMasterDB.setRoomShortName(roomMaster.getRoomShortName());
		
//		roomMasterDB.setRoomTypeId(roomMaster.getRoomTypeId());
//		todo:remove comment
//		roomMasterDB.setRoomStatusTableId(roomMaster.getRoomStatusTableId());
		roomMasterDB.setFloor(roomMaster.getFloor());
//		roomMasterDB.setRoomType(roomMaster.getRoomType());
//		roomMasterDB.setFloor(roomMaster.getFloor());
		roomMasterDB.setHandicap(roomMaster.getHandicap());
		roomMasterDB.setSmoking(roomMaster.getSmoking());
		roomMasterDB.setNonRoom(roomMaster.getNonRoom());
		
		entityManager.flush();
				//again i am taking updated result of book and returning the book object
		RoomMaster updatedRoomMaster = getRoomMaster(roomMasterId);
		return updatedRoomMaster;
	}
	

	public boolean deleteRoomMaster(Long roomMasterId) {
		RoomMaster roomMaster = getRoomMaster(roomMasterId);
		entityManager.remove(roomMaster);
		
		//we are checking here that whether entityManager contains earlier deleted book or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(roomMaster);
		if(status){
			return false;
		}
		return true;
	}
	
//	private Floor RoomStatus(){
//		String hql = "from Floor order by id DESC";
//		Query query = entityManager.createQuery(hql);
//		query.setMaxResults(1);
//		Floor floor = (Floor)query.getSingleResult();
//		return floor;
//	}

	@Override
	public RoomMaster findById(Integer id) {
		return entityManager.find(RoomMaster.class, id);
	}

}
