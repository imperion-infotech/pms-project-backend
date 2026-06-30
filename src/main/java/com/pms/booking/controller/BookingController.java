/**
 * 
 */
package com.pms.booking.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pms.booking.Booking;
import com.pms.booking.BookingRequest;
import com.pms.booking.service.BookingServiceImpl;

/**
 * 
 */
@RestController
@CrossOrigin("*")
public class BookingController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
	
	 @Autowired
	    private BookingServiceImpl service;

	 	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'BOOKING_CREATE')")		
	 	@PostMapping("/user/createbooking")
	    public Long create(@RequestBody BookingRequest req) {
	        return service.createBooking(req);
	    }

	 	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'BOOKING_VIEW')")		
	    @GetMapping("/user/getbookings")
	    public List<Booking> getAll() {
	        return service.getAllBookings();
	    }
	    
	 	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'BOOKING_VIEW')")
	    @GetMapping("/user/getbookingById/{id}")
	    public Booking getBookingById(@PathVariable Long id) {
	        return service.getBookingById(id);
	    }
	    
	    /*
	     * UPDATE BOOKING
	     */
	 	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'BOOKING_UPDATE')")
	    @PutMapping("/user/updatebooking/{id}")
	    public Booking updateBooking(
	            @PathVariable Long id,
	            @RequestBody BookingRequest req) {

	        return service.updateBooking(id, req);
	    }

	    /*
	     * DELETE BOOKING
	     */
	 	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'BOOKING_DELETE')")
	    @DeleteMapping("/user/deletebooking/{id}")
	    public String deleteBooking(@PathVariable Long id) {

	        service.deleteBooking(id);

	        return "Booking deleted successfully";
	    }
	    
	 	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'BOOKING_VIEW_BY_GUEST_DETAILS')")
	    @GetMapping("/user/guestdetails/{guestDetailsId}")
	    public ResponseEntity<Booking> getBookingByGuestDetailsId(
	            @PathVariable Long guestDetailsId) {

	        Booking booking = service.getBookingByGuestDetailsId(guestDetailsId);

	        return ResponseEntity.ok(booking);
	    }

}
