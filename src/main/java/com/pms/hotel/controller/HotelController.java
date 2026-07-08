/**
 * 
 */
package com.pms.hotel.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.hotel.entity.Hotel;
import com.pms.hotel.entity.HotelRequestDTO;
import com.pms.hotel.entity.HotelResponseDTO;
import com.pms.hotel.entity.HotelUpdateRequestDTO;
import com.pms.hotel.entity.PropertyByIdDto;
import com.pms.hotel.service.IHotelService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * 
 */
@RestController
//@RequestMapping("/hotels")
public class HotelController {
	
	static final Logger logger = LoggerFactory.getLogger(HotelController.class);
	
	@Autowired
	IHotelService service;
    
	
//	@PreAuthorize("hasAuthority('HOTEL_CREATE')")
//	@PreAuthorize("hasRole('SUPER_ADMIN')")
//	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'HOTEL_CREATE')")
	@PostMapping(value ="/admin/createhotel", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createHotel(@RequestBody HotelRequestDTO  hotel) {
		
		try {
			Hotel savedHotel = service.createHotel(hotel);
			return ResponseEntity.ok(savedHotel);
		} catch (Exception e) {
			logger.error("Exception in controller of createhotel api :"+e.getMessage() );
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the Hotel");
		}
	}
	
//	@PreAuthorize("hasRole('SUPER_ADMIN')")
//	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'HOTEL_UPDATE')")
//	@PreAuthorize("hasAuthority('HOTEL_UPDATE')")
	@PutMapping("/admin/updatehotel/{id}")
	public ResponseEntity<?> updateHotel(
	        @PathVariable Long id,
	        @RequestBody @Valid HotelUpdateRequestDTO request,
	        HttpSession session) {

	    try {

	        Hotel updatedHotel = service.updateHotel(id, request);

	        return ResponseEntity.ok(updatedHotel);

	    } catch (Exception e) {
	        logger.error("Exception in updateHotel API: {}", e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error while updating hotel");
	    }
	}
	
//	@PreAuthorize("hasRole('SUPER_ADMIN')")
//	@PreAuthorize("hasAuthority('HOTEL_DELETE')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'HOTEL_DELETE')")
	@DeleteMapping("/admin/deletehotel/{id}")
	public ResponseEntity<String> deleteHotel(@PathVariable("id") Long id) {
		String response = service.deleteHotel(id);
		if (!response.isEmpty()) {
			String responseContent = "Hotel has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting Hotel from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/user/gethotels")
//	@PreAuthorize("hasAuthority('HOTEL_VIEW')")
	 @PreAuthorize("@permissionChecker.hasPermission(authentication, 'HOTEL_VIEW')")
//	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public ResponseEntity<List<HotelResponseDTO>> getHotels() {
		 logger.info("Inside getHotels()");
		  logger.info("========== ENTERED getHotels ==========");
	    List<HotelResponseDTO> hotels = service.getHotels();
	    logger.info("========== EXITING getHotels ===================");
	    return ResponseEntity.ok(hotels);
	}
	
//	@PreAuthorize("hasAuthority('HOTEL_VIEW')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'HOTEL_VIEW')")
//	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@GetMapping("/user/gethotel/{id}")
	public ResponseEntity<HotelResponseDTO> getHotel(@PathVariable("id") Long id) {
		 logger.info("Inside getHotel()");
	    HotelResponseDTO hotel = service.getHotel(id);
	    return ResponseEntity.ok(hotel);
	}
	
//	@PreAuthorize("hasRole('SUPER_ADMIN')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'HOTEL_SEARCH')")
//	@PreAuthorize("hasAuthority('HOTEL_SEARCH')")
    @GetMapping("/user/search")
    public List<Hotel> searchHotel(
            @RequestParam(required = false) String hotelName,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String country,
    		@RequestParam(required = false) String email,
			@RequestParam(required = false) String status)
	{

        return service.search(hotelName,url, email, address, state,country,email,status);
    }
    
    @GetMapping("/user/protpertyById/{id}")
    public ResponseEntity<PropertyByIdDto> getPropertyById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPropertyById(id));
    }
	
	

}
