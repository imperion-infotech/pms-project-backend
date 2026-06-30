/**
 * 
 */
package com.pms.rent.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pms.floor.entity.Floor;
import com.pms.rent.RentDetails;
import com.pms.rent.dao.IRentDetailsDAO;
import com.pms.room.entity.RoomMaster;
import com.pms.roomstatus.entity.RoomStatus;
import com.pms.roomtype.entity.RoomType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * 
 */
@Transactional
@Repository
public class RentDetailsDAOImpl  implements IRentDetailsDAO {
	
static final Logger logger = LoggerFactory.getLogger(RentDetailsDAOImpl.class);

	@Autowired
	private RentDetailsRepository rentDetailsRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@SuppressWarnings("unchecked")
	public List<RentDetails> getRentDetails() {
//		String hql = "FROM RentDetails as atcl ORDER BY atcl.id";
		return  rentDetailsRepository.findAll();
	}
	
	public RentDetails getRentDetail(int rentDetailsId)
	{
		return entityManager.find(RentDetails.class, rentDetailsId);
	}
	
	public RentDetails createRentDetail(RentDetails rentDetail)
	{
		
		RentDetails	rDetails = rentDetailsRepository.saveAndFlush(rentDetail);
	
//		entityManager.persist(rentDetail);
//		RentDetails b = getLastInsertedRentDetail();
		return rDetails;
	}
	
	public RentDetails updateRentDetail(int rentDetailsId,RentDetails rentDetail) {
		//First We are taking Book detail from database by given book id and 
				// then updating detail with provided book object
		RentDetails rentDetailsDB = getRentDetail(rentDetailsId);
		rentDetailsDB.setBalance(rentDetail.getBalance());
		rentDetailsDB.setBasic(rentDetail.getBasic());
		rentDetailsDB.setCcAuthorized(rentDetail.getCcAuthorized());
		rentDetailsDB.setDeposite(rentDetail.getDeposite());
		rentDetailsDB.setDiscount(rentDetail.getDiscount());
		rentDetailsDB.setOtherCharges(rentDetail.getOtherCharges());
		rentDetailsDB.setPayments(rentDetail.getPayments());
		rentDetailsDB.setRent(rentDetail.getRent());
		rentDetailsDB.setTaxMaster(rentDetail.getTaxMaster());
		rentDetailsDB.setTotalCharges(rentDetail.getTotalCharges());
		rentDetailsDB.setTotalRental(rentDetail.getTotalRental());
		rentDetailsDB.setCarryForwardAmount(rentDetail.getCarryForwardAmount());
		entityManager.flush();
		RentDetails updatedRentDetails = getRentDetail(rentDetailsId);
		return updatedRentDetails;
	}
	
	public boolean deleteRentDetail(int rentDetailsId) {
		
		RentDetails rentDetails = getRentDetail(rentDetailsId);
		entityManager.remove(rentDetails);
		
		//we are checking here that whether entityManager contains earlier deleted book or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(rentDetails);
		if(status){
			return false;
		}
		return true;
	}
	
	public RentDetails findById(Long id) {
		return entityManager.find(RentDetails.class, id);
	}
	
	private RentDetails getLastInsertedRentDetail(){
		
		String hql = "from RentDetails order by id DESC";
		Query query = entityManager.createQuery(hql);
		query.setMaxResults(1);
		RentDetails rentDetails = (RentDetails)query.getSingleResult();
		return rentDetails;
	}

}
