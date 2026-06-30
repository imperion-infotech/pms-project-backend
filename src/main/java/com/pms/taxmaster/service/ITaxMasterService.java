/**
 * 
 */
package com.pms.taxmaster.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.floor.entity.Floor;
import com.pms.taxmaster.entity.TaxMaster;

/**
 * 
 */
public interface ITaxMasterService {
	
static final Logger logger = LoggerFactory.getLogger(ITaxMasterService.class);
	
	List<TaxMaster> getTaxMasters();
	TaxMaster createTaxMaster(TaxMaster taxMaster);
	TaxMaster updateTaxMaster(Long taxMasterId, TaxMaster taxMaster);
	TaxMaster getTaxMaster(int taxMasterId);
	boolean deleteTaxMaster(int taxMasterId);
	TaxMaster getTaxMasterByIdAndHotelID(Integer id);
	public List<TaxMaster> search(String name,String description);

}
