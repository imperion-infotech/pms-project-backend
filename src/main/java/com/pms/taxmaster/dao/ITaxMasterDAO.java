/**
 * 
 */
package com.pms.taxmaster.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.room.entity.RoomMaster;
import com.pms.taxmaster.entity.TaxMaster;

/**
 * 
 */
public interface ITaxMasterDAO {
	
static final Logger logger = LoggerFactory.getLogger(ITaxMasterDAO.class);
	
	public List<TaxMaster> getTaxMasters();
	public TaxMaster getTaxMaster(Long taxMasterId);
	public TaxMaster createTaxMaster(TaxMaster taxMaster);
	public TaxMaster updateTaxMaster(Long taxMasterId,TaxMaster taxMaster);
	public boolean deleteTaxMaster(Long taxMasterId);
	public TaxMaster findById(Integer id);


}
