/**
 * 
 */
package com.pms.nightaudit.service.impl;


import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pms.nightaudit.repository.FolioTransactionRepository;
import com.pms.nightaudit.service.IRevenueReportService;
import com.pms.security.configuration.HotelContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RevenueReportServiceImpl
        implements IRevenueReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(RevenueReportServiceImpl.class);

    private final FolioTransactionRepository repository;

    @Override
    public Double generateDailyRevenue() {

        Double revenue =
                repository.getTotalRevenue(
                        HotelContext.getHotelId(),
                        LocalDate.now());

        log.info(
                "Today's Revenue : {}",
                revenue);

        return revenue;
    }
}