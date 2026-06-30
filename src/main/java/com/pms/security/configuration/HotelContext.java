/**
 * 
 */
package com.pms.security.configuration;

/**
 * 
 */
public class HotelContext {

	 private static final ThreadLocal<Long> CURRENT_HOTEL = new ThreadLocal<>();

	    public static void setHotelId(Long hotelId) {
	        CURRENT_HOTEL.set(hotelId);
	    }

	    public static Long getHotelId() {
	        return CURRENT_HOTEL.get();
	    }

	    public static void clear() {
	        CURRENT_HOTEL.remove();
	    }

}

