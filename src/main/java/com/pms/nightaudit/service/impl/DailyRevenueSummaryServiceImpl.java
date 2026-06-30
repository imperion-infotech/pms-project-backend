/**
 * 
 */
package com.pms.nightaudit.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.pms.nightaudit.entity.DailyRevenueSummary;
import com.pms.nightaudit.repository.DailyRevenueSummaryRepository;
import com.pms.nightaudit.service.IBusinessDateService;
import com.pms.nightaudit.service.IDailyRevenueSummaryService;
import com.pms.security.configuration.HotelContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailyRevenueSummaryServiceImpl implements IDailyRevenueSummaryService {

	private final DailyRevenueSummaryRepository repository;

	private final IBusinessDateService businessDateService;

	@Override
	public void saveRevenueSummary(BigDecimal roomRevenue, BigDecimal taxRevenue) {

		DailyRevenueSummary summary = new DailyRevenueSummary();

		summary.setHotelId(HotelContext.getHotelId());

		summary.setBusinessDate(businessDateService.getBusinessDate());

		summary.setRoomRevenue(roomRevenue);

		summary.setTaxRevenue(taxRevenue);

		summary.setTotalRevenue(roomRevenue.add(taxRevenue));

		repository.save(summary);
	}
}