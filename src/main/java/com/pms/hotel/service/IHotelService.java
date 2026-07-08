/**
 * 
 */
package com.pms.hotel.service;

import java.util.List;

import com.pms.hotel.entity.Hotel;
import com.pms.hotel.entity.HotelRequestDTO;
import com.pms.hotel.entity.HotelResponseDTO;
import com.pms.hotel.entity.HotelUpdateRequestDTO;
import com.pms.hotel.entity.PropertyByIdDto;

/**
 * 
 */
public interface IHotelService {
	
	List<HotelResponseDTO> getHotels();
	Hotel createHotel(HotelRequestDTO  req);
	Hotel updateHotel(Long hotelId, HotelUpdateRequestDTO existingHotel);
	HotelResponseDTO getHotel(Long hotelId) ;
	String deleteHotel(Long hotelId);
	Hotel getHotelById(Long id);
    List<Hotel> search(String hotelName, String url,String address,String city, String state, String country , String email ,String status);
	
	PropertyByIdDto getPropertyById(Long id);

}
