/**
 * 
 */
package com.pms.othercharge.service;

import java.util.List;

import com.pms.othercharge.entity.OtherCharge;

/**
 * 
 */
public interface IOtherChargeService {

	List<OtherCharge> getOtherCharges();
	OtherCharge createOtherCharge(OtherCharge otherCharge);
	OtherCharge updateOtherCharge(Long otherChargeId, OtherCharge otherCharge);
	OtherCharge getOtherChargeById(Long id);
	public boolean deleteOtherCharge(Long otherChargeId);
	public List<OtherCharge> search(String otherChargeName,String otherChargeShortName, String categoryName);
}
