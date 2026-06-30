/**
 * 
 */
package com.pms.booking.service;

import java.util.List;

import com.pms.booking.Booking;
import com.pms.booking.BookingRequest;

/**
 * 
 */
public interface IBookingService {
	
	public Long createBooking(BookingRequest req);
	
	 public List<Booking> getAllBookings();
	 
	 Booking updateBooking(Long id, BookingRequest req);

	 Booking deleteBooking(Long id);
	 
	 Booking getBookingByGuestDetailsId(Long guestDetailsId);
	

}
