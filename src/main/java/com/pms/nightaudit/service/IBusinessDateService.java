/**
 * 
 */
package com.pms.nightaudit.service;

import java.time.LocalDate;

import com.pms.nightaudit.entity.BusinessDate;
import com.pms.nightaudit.entity.BusinessDateRequest;

public interface IBusinessDateService {

	 LocalDate getBusinessDate();

	 void moveToNextDay();
	    
	 BusinessDate addBusinessDate(BusinessDateRequest request);
}