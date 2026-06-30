/**
 * 
 */
package com.pms.security.dto;

import java.util.List;

import com.pms.security.entity.RefreshToken;

/**
 * 
 */
public class LoginResponse {
	
	 private String token;
	 private List<HotelDTO> hotels;
	 private String refreshToken;
	 public String getToken() {
		 return token;
	 }
	 public void setToken(String token) {
		 this.token = token;
	 }
	 public List<HotelDTO> getHotels() {
		 return hotels;
	 }
	 public void setHotels(List<HotelDTO> hotels) {
		 this.hotels = hotels;
	 }
	 public String getRefreshToken() {
		 return refreshToken;
	 }
	 public void setRefreshToken(String refreshToken) {
		 this.refreshToken = refreshToken;
	 }
	 
}
