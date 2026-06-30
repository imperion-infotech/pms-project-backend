/**
 * 
 */
package com.pms.guestdetails.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.guestdetails.GuestDetails;

/**
 * 
 */

public interface IGuestDetailsDAO {

	
static final Logger logger = LoggerFactory.getLogger(IGuestDetailsDAO.class);
	
	public List<GuestDetails> getGuestDetails();
	public GuestDetails getGuestDetail(Long guestDetailsId);
	public GuestDetails createGuestDetails(GuestDetails guestDetails);
	public GuestDetails updateGuestDetails(Long guestDetailsId,GuestDetails guestDetails);
	public GuestDetails findById(Integer id);
	public boolean deleteGuestDetails(Long guestDetailsId);
}
