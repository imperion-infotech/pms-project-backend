package com.pms.booking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.auditlog.annotation.Auditable;
import com.pms.booking.Booking;
import com.pms.booking.BookingRequest;
import com.pms.bookinghistory.entity.BookingHistory;
import com.pms.bookinghistory.enums.BookingHistoryAction;
import com.pms.bookinghistory.repository.BookingHistoryRepository;
import com.pms.common.service.SoftDeleteService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.service.IGuestDetailsService;
import com.pms.personaldetails.PersonalDetails;
import com.pms.personaldetails.service.IPersonalDetailsService;
import com.pms.rent.RentDetails;
import com.pms.rent.dao.impl.RentDetailsRepository;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.repository.BookingRepository;
import com.pms.security.repository.RoomRepository;
import com.pms.security.service.BaseHotelService;
import com.pms.stay.entity.RateTypeEnum;
import com.pms.stay.entity.StayDetails;
import com.pms.stay.entity.StayStatusEnum;
import com.pms.stay.service.IStayDetailsService;
import com.pms.tax.service.IGuestTaxTransactionService;

import jakarta.transaction.Transactional;

@Service
public class BookingServiceImpl extends BaseHotelService implements IBookingService {
	
	private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

		@Autowired
	    private BookingRepository bookingRepo;

	    @Autowired
	    private RoomRepository roomRepo;
	    
	    @Autowired
	     private SoftDeleteService softDeleteService;
	    
	    @Autowired
	     private IPersonalDetailsService personalDetailsService;
	    
	    @Autowired
	     private IStayDetailsService stayDetailsService;
	    
	    @Autowired
	     private IGuestDetailsService guestDetailsService;
	    
	    @Autowired
	    private IGuestTaxTransactionService guestTaxTransactionService;
	    
	    @Autowired
	    private RentDetailsRepository rentDetailsRepository;
	    
	    @Autowired
	    private BookingHistoryRepository bookingHistoryRepository;
	    
	    
	    @Auditable(action = "CREATE", entity = "BOOKINGS")
	    @Transactional
	    public Long createBooking(BookingRequest req) {

//	        RoomMaster roomMaster = roomRepo.findFirstByRoomStatus_RoomStatusName("AVAILABLE");
//	        roomMaster.setRoomStatus("BOOKED");
	    	GuestDetails createdGuestDetails = null;
	    	try {
	    	Long hotelId = HotelContext.getHotelId();
			if (hotelId == null) {
				throw new ResourceNotFoundException("Hotel not selected");
			}
			
			
			Long userId = UserContext.getUserId();

			if (userId == null) {
				throw new ResourceNotFoundException("User not selected");
			}

	        Booking booking = new Booking();
	        booking.setFirstName(req.getFirstName());
	        booking.setLastName(req.getLastName());
	        booking.setCheckIn(req.getCheckIn());
	        booking.setCheckOut(req.getCheckOut());
	        booking.setStatus(req.getStatus());
	        booking.setHotelId(req.getHotelId());
	        booking.setRoomTypeId(req.getRoomTypeId());
	        booking.setRoomFeatures(req.getRoomFeatures());
	        booking.setCreatedBy(userId);
	        booking.setSource(req.getSource());
	        booking.setBalance(req.getBalance());
	        booking.setPaymentAmount(req.getPaymentAmount());
	        
	        assignHotel(booking, booking.getHotelId());
	        
	        PersonalDetails pDetails = new PersonalDetails();
	        pDetails.setFirstName(req.getFirstName());
	        pDetails.setLastName(req.getLastName());
//	        pDetails.setCompanyName("test");
	        pDetails.setCreatedBy(userId);
	        pDetails.setHotelId(hotelId);
	        PersonalDetails createdPdetails = personalDetailsService.create(pDetails);
	        
	        StayDetails stayDetails = new StayDetails();
	        stayDetails.setPersonalDetailsId(createdPdetails.getId());
	        stayDetails.setRoomTypeId(req.getRoomTypeId());
	        stayDetails.setRateTypeEnum(RateTypeEnum.RACK);
	        stayDetails.setStayStatusEnum(StayStatusEnum.Confirmed);
	        stayDetails.setCreatedBy(userId);
	        stayDetails.setHotelId(hotelId);
	        stayDetails.setNoOfGuest(0);
	        StayDetails createdstayDetails = stayDetailsService.createStayDetails(stayDetails);
	        
	        GuestDetails gDetails =new GuestDetails();
	        gDetails.setPersonalDetailsId(createdPdetails.getId().intValue());
	        gDetails.setStayDetailsId(createdstayDetails.getId().intValue());
	        gDetails.setCheckInDate(req.getCheckIn());
	        gDetails.setCheckOutDate(req.getCheckOut());
	        gDetails.setHotelId(hotelId);
	        gDetails.setCreatedBy(userId);
	        createdGuestDetails =  guestDetailsService.createGuestDetail(gDetails);
	        
	        RentDetails rentDetails = new RentDetails();
	        rentDetails.setPersonalDetailsId(pDetails.getId());
	        rentDetails.setHotelId(hotelId);
	        RentDetails createdRentDetails = rentDetailsRepository.save(rentDetails);
	        booking.setRentDetailsId(createdRentDetails.getId());
	        
	        
	        booking.setGuestDetailsId(createdGuestDetails.getId());
	        Booking createdBooking = bookingRepo.save(booking);
	        
	        guestTaxTransactionService.saveGuestTaxes(createdBooking.getId());
	        
//	        folioTransactionService.postRoomRent(booking);
	        
//	        folioTransactionService.postTaxes(booking);
	        
	        
	        BookingHistory history = new BookingHistory();

	        BeanUtils.copyProperties(booking, history);

	        history.setBookingId(booking.getId());
	        history.setActionType(booking.getStatus());
	        history.setCreatedOn(LocalDateTime.now());
	        

	        bookingHistoryRepository.save(history);
	    	}
	    	catch(Exception e) {
	    		
	    		logger.error("Exception in create Booking api"+e.getMessage());
	    	}
	        
	        return createdGuestDetails.getId();
	    }
	    
	    public Booking getBookingById(Long id) {
	    	 Long hotelId = HotelContext.getHotelId();
		        
		        if (hotelId == null) {
			        throw new ResourceNotFoundException("Hotel not selected");
			    }
	    	
	    	Booking booking = bookingRepo.findByIdAndIsActiveTrueAndIsDeletedFalseAndHotelId(id,hotelId)
	    	        .orElseThrow(() -> new RuntimeException("Booking not found"));
	    	
	    	
	    	return booking;
	    	
	    	
	    }

	    public List<Booking> getAllBookings() {
//	        return bookingRepo.findAll();
	        
	        Long hotelId = HotelContext.getHotelId();
	        
	        if (hotelId == null) {
		        throw new ResourceNotFoundException("Hotel not selected");
		    }
		    validateHotelAccess(hotelId);
		    List<Booking> bookingList;
		    if (isSuperAdmin()) 
		    	bookingList = bookingRepo.findByIsDeletedFalseAndIsActiveTrue();
		    else 
	         bookingList = bookingRepo.findByIsDeletedFalseAndIsActiveTrueAndHotelId(hotelId);
		    
		    logger.info("BOOKING LIST COUNT ::"+bookingList.size());
		    
		    return bookingList;
	        
	    }
	    
	    @Auditable(action = "UPDATE", entity = "BOOKINGS")
	    @Override
	    public Booking updateBooking(Long id, BookingRequest req) {

	        Booking booking = bookingRepo.findById(id)
	                .orElseThrow(() ->
	                        new RuntimeException("Booking not found"));

	        booking.setFirstName(req.getFirstName());
	        booking.setLastName(req.getLastName());
	        booking.setRoomTypeId(req.getRoomTypeId());
	        booking.setCheckIn(req.getCheckIn());
	        booking.setCheckOut(req.getCheckOut());
	        booking.setHotelId(req.getHotelId());
	        booking.setStatus(req.getStatus());
	        booking.setGuestDetailsId(req.getGuestDetailsId());
	        booking.setRentDetailsId(req.getRentDetailsId());
	        booking.setSource(req.getSource());
	        booking.setBalance(req.getBalance());
	        booking.setPaymentAmount(req.getPaymentAmount());
	        booking.setBookingRefNo(req.getBookingRefNo());
	        
//	        GuestDetails guestDetails =  guestDetailsService.getGuestDetail(req.getGuestDetailsId());
	        
			Long hotelId = HotelContext.getHotelId();
			validateHotelAccess(hotelId);
	        
	        
	        BookingHistory history = new BookingHistory();

	        BeanUtils.copyProperties(booking, history);

	        history.setBookingId(booking.getId());
	        history.setActionType(req.getStatus());
	        history.setCreatedOn(LocalDateTime.now());

	        bookingHistoryRepository.save(history);

	       

	        return bookingRepo.save(booking);
	    }

	    @Auditable(action = "DELETE", entity = "DISCOUNTS")
	    @Override
	    public Booking deleteBooking(Long id) {
	    	
	    	 Long hotelId = HotelContext.getHotelId();
		        
		        if (hotelId == null) {
			        throw new ResourceNotFoundException("Hotel not selected");
			    }

	        Booking booking = bookingRepo.findByIdAndIsActiveTrueAndIsDeletedFalseAndHotelId(id,hotelId)
	                .orElseThrow(() ->
	                        new RuntimeException("Booking not found"));

	        validateHotelAccess(booking.getHotelId());
	        Booking c =  softDeleteService.softDelete(id, bookingRepo);
	        
	        
	        BookingHistory history = new BookingHistory();

	        BeanUtils.copyProperties(c, history);

	        history.setBookingId(c.getId());
	        history.setActionType("DELETED");
	        history.setCreatedOn(LocalDateTime.now());

	        bookingHistoryRepository.save(history);
	        
	        
	        return c;
	        
	    }
	    
	    @Override
	    public Booking getBookingByGuestDetailsId(Long guestDetailsId) {
	    	
	    	Long hotelId = HotelContext.getHotelId();
			validateHotelAccess(hotelId);

//	        return bookingRepo.findByGuestDetailsId(guestDetailsId)
//	                .orElseThrow(() -> new RuntimeException(
//	                        "Booking not found for guestDetailsId : " + guestDetailsId));
			
			List<Booking> bookings  = bookingRepo.findByGuestDetailsId(guestDetailsId);

			if (bookings == null) {
			    throw new RuntimeException("Booking not found for guestDetailsId: " + guestDetailsId);
			}
	        
	        return bookings.get(0);
	        
	    }

}
