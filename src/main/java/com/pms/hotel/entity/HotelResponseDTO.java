/**
 * 
 */
package com.pms.hotel.entity;

import java.util.List;

import com.pms.floor.entity.FloorDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */

@Builder
@Getter 
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDTO {
	
	 	private Long id;
	 	private String hotelName;
		private String url;
	    private String address;
	    private String city;
	    private String state;
	    private String country;
	    private String zipCode;
	    private String email;
	    private String contactNumber;
	    private String timezone;
	    private String hotelLogo;
	    private String hotelImage;
	    private String status;

	    // Optional (safe version)
	    private List<FloorDTO> floors;

}
