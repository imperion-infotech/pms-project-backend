/**
 * 
 */
package com.pms.nightaudit.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
public interface IRevenueReportService {


    public Double generateDailyRevenue();

}