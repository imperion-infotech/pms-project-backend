/**
 * 
 */
package com.pms.hotel.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.pms.floor.entity.Floor;
import com.pms.floor.entity.FloorDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

	    /*
		public List<FloorDTO> getFloors() {
			return floors;
		}

		public void setFloors(List<FloorDTO> floors) {
			this.floors = floors;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getHotelName() {
			return hotelName;
		}

		public void setHotelName(String hotelName) {
			this.hotelName = hotelName;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getZipCode() {
			return zipCode;
		}

		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}

		public String getTimezone() {
			return timezone;
		}

		public void setTimezone(String timezone) {
			this.timezone = timezone;
		}

		public String getHotelLogo() {
			return hotelLogo;
		}

		public void setHotelLogo(String hotelLogo) {
			this.hotelLogo = hotelLogo;
		}

		public String getHotelImage() {
			return hotelImage;
		}

		public void setHotelImage(String hotelImage) {
			this.hotelImage = hotelImage;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}*/
		
		
	    
}
