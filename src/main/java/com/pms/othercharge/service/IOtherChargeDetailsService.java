/**
 * 
 */
package com.pms.othercharge.service;

import java.util.List;

import com.pms.othercharge.entity.OtherCharge;
import com.pms.othercharge.entity.OtherChargeDetails;
import com.pms.othercharge.entity.OtherChargeDetailsResponseDTO;

/**
 * 
 */
public interface IOtherChargeDetailsService {
	
	List<OtherChargeDetails> getAllOtherChargeDetails();
	OtherChargeDetails createOtherChargeDetails(OtherChargeDetails otherChargeDetails);
	OtherChargeDetails updateOtherChargeDetails(Long otherChargeDetailsId, OtherChargeDetails otherChargeDetails);
	OtherChargeDetails getOtherChargeDetailsById(Long id);
	public boolean deleteOtherChargeDetails(Long otherChargeDetailsId);
	
	public List<OtherChargeDetailsResponseDTO> getOtherChargeDetailsByGuestId(Long guestId);
}
