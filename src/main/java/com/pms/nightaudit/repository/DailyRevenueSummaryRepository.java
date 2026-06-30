package com.pms.nightaudit.repository;

import com.pms.nightaudit.entity.DailyRevenueSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRevenueSummaryRepository
        extends JpaRepository<DailyRevenueSummary, Long> {
}