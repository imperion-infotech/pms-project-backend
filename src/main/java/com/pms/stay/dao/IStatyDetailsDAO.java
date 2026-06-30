/**
 * 
 */
package com.pms.stay.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.stay.entity.StayDetails;

/**
 * 
 */

public interface IStatyDetailsDAO {
	
static final Logger logger = LoggerFactory.getLogger(IStatyDetailsDAO.class);
	
	public List<StayDetails> getStayDetails();
	public StayDetails getStayDetail(int stayDetailsId);
	public StayDetails createStayDetails(StayDetails stayDetails);
	public StayDetails updateStayDetails(int stayDetailsId,StayDetails stayDetails);
	public boolean deleteStayDetails(int stayDetailsId);
	public StayDetails findById(Long id);

}
