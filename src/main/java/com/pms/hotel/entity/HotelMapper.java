/**
 * 
 */
package com.pms.hotel.entity;

import java.util.ArrayList;
import java.util.List;
import com.pms.floor.entity.Floor;
import com.pms.floor.entity.FloorDTO;

/**
 * 
 */
public class HotelMapper {
	
	public static Hotel toEntity(HotelRequestDTO dto) {

	    Hotel hotel = new Hotel();

	    hotel.setHotelName(dto.getHotelName());
	    hotel.setUrl(dto.getUrl());
	    hotel.setAddress(dto.getAddress());
	    hotel.setCity(dto.getCity());
	    hotel.setState(dto.getState());
	    hotel.setCountry(dto.getCountry());
	    hotel.setZipCode(dto.getZipCode());
	    hotel.setEmail(dto.getEmail());
	    hotel.setContactNumber(dto.getContactNumber());
	    hotel.setStatus(dto.getStatus());
	    hotel.setTimezone(dto.getTimezone());
	    return hotel;
	}

	public static HotelResponseDTO toDTO(Hotel hotel) {

	    List<FloorDTO> floorDTOs = new ArrayList<>();

	    if (hotel.getFloors() != null) {
	        for (Floor f : hotel.getFloors()) {

	            FloorDTO dto = new FloorDTO();
	            dto.setId(f.getId());
	            dto.setName(f.getName());
	            dto.setNoOfRooms(f.getNoOfRooms());

	            floorDTOs.add(dto);
	        }
	    }
	    
	    

	    HotelResponseDTO dto = new HotelResponseDTO();
	    dto.setId(hotel.getId());
	    dto.setHotelName(hotel.getHotelName());
	    dto.setAddress(hotel.getAddress());
	    dto.setCity(hotel.getCity());
	    dto.setState(hotel.getState());
	    dto.setCountry(hotel.getCountry());
	    dto.setZipCode(hotel.getZipCode());
	    dto.setEmail(hotel.getEmail());
	    dto.setContactNumber(hotel.getContactNumber());
	    dto.setStatus(hotel.getStatus());
	    dto.setTimezone(hotel.getTimezone());
	    dto.setUrl(hotel.getUrl());
	    dto.setFloors(floorDTOs);
	    dto.setHotelImage(hotel.getHotelImage());
	    dto.setHotelLogo(hotel.getHotelLogo());
	    
	    

	    return dto;
	}

}
