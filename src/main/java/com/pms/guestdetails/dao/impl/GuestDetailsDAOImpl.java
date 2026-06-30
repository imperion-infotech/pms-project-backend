/**
 * 
 */
package com.pms.guestdetails.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pms.document.dao.DocumentDetailsRepository;
import com.pms.exception.ResourceNotFoundException;
import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dao.IGuestDetailsDAO;
import com.pms.paymentdetails.entity.PaymentDetails;
import com.pms.paymentdetails.repository.PaymentDetailsRepository;
import com.pms.personaldetails.PersonalDetailsRepository;
import com.pms.rent.dao.impl.RentDetailsRepository;
import com.pms.room.dao.impl.RoomMasterRepository;
import com.pms.security.configuration.HotelContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * 
 */
@Transactional
@Repository
public class GuestDetailsDAOImpl implements IGuestDetailsDAO {

	static final Logger logger = LoggerFactory.getLogger(GuestDetailsDAOImpl.class);

	@Autowired
	private GuestDetailsRepository guestDetailsRepository;

	@Autowired
	private RoomMasterRepository roomMasterRepository;

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	@Autowired
	private DocumentDetailsRepository documentDetailsRepository;

	@Autowired
	private RentDetailsRepository rentDetailsRepository;
	
	@Autowired
	private PaymentDetailsRepository paymentDetailsRepository;
	

	@PersistenceContext
	private EntityManager entityManager;

	public List<GuestDetails> getGuestDetails() {

//		String hql = "FROM GuestDetails as atcl ORDER BY atcl.id";
		return guestDetailsRepository.findAll();
	}

	public GuestDetails getGuestDetail(Long guestDetailsId) {
		return entityManager.find(GuestDetails.class, guestDetailsId);
	}

	@Override
	public GuestDetails createGuestDetails(GuestDetails guestDetails) {

		Long hotelId = HotelContext.getHotelId();
		 if (hotelId == null) {
	         throw new ResourceNotFoundException("Hotel not selected");
	     }
		 PaymentDetails paymentDetails1 = guestDetails.getPaymentDetails().get(0);
		List<PaymentDetails> paymentDetails = paymentDetailsRepository.findByGuestDetailsIdAndHotelId(Long.valueOf(paymentDetails1.getId()+""),hotelId);
		guestDetails.setPaymentDetails(paymentDetails);
		GuestDetails b = guestDetailsRepository.saveAndFlush(guestDetails);
		return b;
	}

	@Override
	public GuestDetails updateGuestDetails(Long guestDetailsId, GuestDetails guestDetails) {

		GuestDetails guestDetailsDB = getGuestDetail(guestDetailsId);
		guestDetailsDB.setCheckInDate(guestDetails.getCheckInDate());
		guestDetailsDB.setCheckInTime(guestDetails.getCheckInTime());
		guestDetailsDB.setCheckOutDate(guestDetails.getCheckOutDate());
		guestDetailsDB.setCheckOutTime(guestDetails.getCheckOutTime());
//		guestDetailsDB.setDocumentDetails(guestDetails.getDocumentDetails());
		guestDetailsDB.setGuestDetailsStatus(guestDetails.getGuestDetailsStatus());
		//guestDetailsDB.setPersonalDetails(guestDetails.getPersonalDetails());
//		guestDetailsDB.setRentDetails(guestDetails.getRentDetails());
		guestDetailsDB.setRoomMaster(guestDetails.getRoomMaster());
		guestDetailsDB.setNoOfDays(guestDetails.getNoOfDays());
		/*RentDetails rent = rentDetailsRepository.findById(guestDetails.getRentDetails().getId())
				.orElseThrow(() -> new RuntimeException("Rent not found"));
		guestDetailsDB.setRentDetails(rent);*/

		// ✅ Extract IDs from request
		/*List<Integer> docIds = guestDetails.getDocumentDetails().stream().map(DocumentDetails::getId).toList();

		// ✅ Fetch MANAGED entities from DB
		List<DocumentDetails> managedDocs = documentDetailsRepository.findAllById(docIds);

		// ✅ SET ONLY managed entities (VERY IMPORTANT)
		guestDetailsDB.setDocumentDetails(managedDocs);

		guestDetailsDB.setDocumentDetails(managedDocs);

		PersonalDetails pDetails = personalDetailsRepository.findById(guestDetails.getPersonalDetails().getId())
				.orElseThrow(() -> new RuntimeException("Personal Details not found"));
		guestDetailsDB.setPersonalDetails(pDetails);*/

//		entityManager.flush();
		guestDetailsRepository.save(guestDetailsDB);
		GuestDetails updatedGuestDetails = getGuestDetail(guestDetailsId);

		return updatedGuestDetails;
	}

	@Override
	public boolean deleteGuestDetails(Long guestDetailsId) {

		GuestDetails guestDetails = getGuestDetail(guestDetailsId);
		entityManager.remove(guestDetails);

		// we are checking here that whether entityManager contains earlier deleted book
		// or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(guestDetails);
		if (status) {
			return false;
		}
		return true;
	}

	public GuestDetails findById(Integer id) {

		return entityManager.find(GuestDetails.class, id);
	}


}
