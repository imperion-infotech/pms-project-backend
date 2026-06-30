/**
 * 
 */
package com.pms.stay.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.stay.dao.IStatyDetailsDAO;
import com.pms.stay.dto.ChangeRoomRequestDTO;
import com.pms.stay.entity.StayDetails;

/**
 * 
 */
public interface IStayDetailsService {
	
static final Logger logger = LoggerFactory.getLogger(IStatyDetailsDAO.class);
	
	public List<StayDetails> getStayDetails();
	public StayDetails getStayDetail(Long stayDetailsId);
	public StayDetails createStayDetails(StayDetails stayDetails);
	public StayDetails updateStayDetails(Long stayDetailsId,StayDetails stayDetails);
	public boolean deleteSoftStayDetails(Long id);
	void changeRoom(ChangeRoomRequestDTO dto);
	


}
