/**
 * 
 */
package com.pms.tax.service;

import java.util.List;

import com.pms.tax.dto.GuestTaxTransactionResponse;

/**
 * 
 */
public interface IGuestTaxTransactionService {
	
	 void saveGuestTaxes(Long bookingId);

	 List<GuestTaxTransactionResponse> getTaxesByBooking(Long bookingId);
	    void updateGuestTaxes(Long bookingId);

}
