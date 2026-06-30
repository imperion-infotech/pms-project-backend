/**
 * 
 */
package com.pms.hotel.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class HotelUpdateRequestDTO {
	
	private String address;
    private String city;
    private String contactNumber;
    private String country;
    private String email;
    private String hotelImage;
    private String hotelLogo;
    private String state;
    private String status;
    private String timezone;
    private String url;
    private String zipCode;
    private Long hotelId;
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
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHotelImage() {
		return hotelImage;
	}
	public void setHotelImage(String hotelImage) {
		this.hotelImage = hotelImage;
	}
	public String getHotelLogo() {
		return hotelLogo;
	}
	public void setHotelLogo(String hotelLogo) {
		this.hotelLogo = hotelLogo;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
    
}
