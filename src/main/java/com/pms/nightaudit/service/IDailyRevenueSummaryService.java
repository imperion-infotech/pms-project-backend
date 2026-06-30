/**
 * 
 */
package com.pms.nightaudit.service;

import java.math.BigDecimal;

public interface IDailyRevenueSummaryService {

    void saveRevenueSummary(
            BigDecimal roomRevenue,
            BigDecimal taxRevenue);
}