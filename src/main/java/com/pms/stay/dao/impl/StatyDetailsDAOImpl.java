/**
 * 
 */
package com.pms.stay.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pms.floor.dao.IFloorDAO;
import com.pms.roomtype.dao.IRoomTypeDAO;
import com.pms.stay.dao.IStatyDetailsDAO;
import com.pms.stay.dao.StayDetailsRepository;
import com.pms.stay.entity.StayDetails;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * 
 */
@Transactional
@Repository
public class StatyDetailsDAOImpl implements IStatyDetailsDAO{
	

static final Logger logger = LoggerFactory.getLogger(StatyDetailsDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private IFloorDAO floorDao;
	
	@Autowired
	private IRoomTypeDAO roomTypeDAO;
	
	@Autowired
	private StayDetailsRepository stayDetailsRepository;
	

	@SuppressWarnings("unchecked")
	public List<StayDetails> getStayDetails() {
		String hql = "FROM StayDetails as atcl ORDER BY atcl.id";
		return (List<StayDetails>) entityManager.createQuery(hql).getResultList();
	}
	
	public StayDetails getStayDetail(int StayDetailsId) {
		return entityManager.find(StayDetails.class, StayDetailsId);
	}

	public StayDetails createStayDetails(StayDetails stayDetails) {
		entityManager.persist(stayDetails);
		StayDetails b = getLastInsertedStayDetails(stayDetails);
		return b;
	}

	public StayDetails updateStayDetails(int stayDetailsId, StayDetails stayDetails) {
		//First We are taking Book detail from database by given book id and 
				// then updating detail with provided book object
		StayDetails stayDetailsDB = getStayDetail(stayDetailsId);
		stayDetailsDB.setBuildingId(stayDetails.getBuildingId());
		stayDetailsDB.setComment(stayDetails.getComment());
		stayDetailsDB.setFloorId(stayDetails.getFloorId());
		stayDetailsDB.setNoOfGuest(stayDetails.getNoOfGuest());
		stayDetailsDB.setRateTypeEnum(stayDetails.getRateTypeEnum());
		stayDetailsDB.setRoomMasterId(stayDetails.getRoomMasterId());
		stayDetailsDB.setRoomTypeId(stayDetails.getRoomTypeId());
		stayDetailsDB.setStayStatusEnum(stayDetails.getStayStatusEnum());
		
		entityManager.flush();
				//again i am taking updated result of book and returning the book object
		StayDetails updatedStayDetails = getStayDetail(stayDetailsId);
		return updatedStayDetails;
	}
	

	public boolean deleteStayDetails(int stayDetailsId) {
		StayDetails stayDetails = getStayDetail(stayDetailsId);
		entityManager.remove(stayDetails);
		
		//we are checking here that whether entityManager contains earlier deleted book or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(stayDetails);
		if(status){
			return false;
		}
		return true;
	}
	
	/**
	 * This method will get the latest inserted record from the database and return the object of Book class
	 * @return book
	 */
	private StayDetails getLastInsertedStayDetails(StayDetails stayDetails){
//		String hql = "from StayDetails order by id DESC";
//		Query query = entityManager.createQuery(hql);
//		query.setMaxResults(1);
//		StayDetails stayDetails = (StayDetails)query.getSingleResult();
		
		return stayDetailsRepository.saveAndFlush(stayDetails);
	}


	@Override
	public StayDetails findById(Long id) {
		return entityManager.find(StayDetails.class, id);
	}

}
