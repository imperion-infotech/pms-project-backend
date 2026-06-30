/**
 * 
 */
package com.pms.rent.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.rent.RentDetails;
import com.pms.room.entity.RoomMaster;

/**
 * 
 */
public interface IRentDetailsDAO {
	
static final Logger logger = LoggerFactory.getLogger(IRentDetailsDAO.class);
	
	public List<RentDetails> getRentDetails();
	public RentDetails getRentDetail(int rentDetailsId);
	public RentDetails createRentDetail(RentDetails rentDetails);
	public RentDetails updateRentDetail(int rentDetailsId,RentDetails rentDetails);
	public boolean deleteRentDetail(int rentDetailsId);
	public RentDetails findById(Long id);

}
