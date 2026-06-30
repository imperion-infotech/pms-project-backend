/**
 * 
 */
package com.pms.nightaudit.service.impl;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pms.nightaudit.service.IAutoCheckoutService;
import com.pms.nightaudit.service.IBusinessDateService;
import com.pms.nightaudit.service.INightAuditRoomStatusService;
import com.pms.nightaudit.service.INightAuditService;
import com.pms.nightaudit.service.INoShowService;
import com.pms.nightaudit.service.IRevenueReportService;
import com.pms.nightaudit.service.IRoomChargePostingService;
import com.pms.nightaudit.service.ITaxPostingService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NightAuditServiceImpl implements INightAuditService {

	private static final Logger logger = LoggerFactory.getLogger(NightAuditServiceImpl.class);

	private final IBusinessDateService businessDateService;

	private final INoShowService noShowService;

	private final IAutoCheckoutService autoCheckoutService;

	private final INightAuditRoomStatusService nightAuditRoomStatusService;

	private final IRoomChargePostingService roomChargePostingService;

	private final ITaxPostingService taxPostingService;

	private final IRevenueReportService revenueReportService;

	@Override
	public void runNightAudit() {

		log.info("Night Audit Started");

		LocalDate businessDate = businessDateService.getBusinessDate();

		noShowService.processNoShows(businessDate);

		autoCheckoutService.processDueOutGuests(businessDate);

		nightAuditRoomStatusService.updateRoomStatuses();

		// TODO:Following services are working on foliotransaction entity

		/*
		 * roomChargePostingService .postDailyRoomCharges();
		 * 
		 * taxPostingService.postTaxes(1000.00);
		 * 
		 * 
		 * revenueReportService .generateDailyRevenue();
		 */

		businessDateService.moveToNextDay();

		log.info("Night Audit Completed");
	}
}