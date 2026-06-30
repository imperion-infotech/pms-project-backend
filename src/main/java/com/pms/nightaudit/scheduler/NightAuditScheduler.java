/**
 * 
 */
package com.pms.nightaudit.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pms.nightaudit.service.INightAuditService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class NightAuditScheduler {

	@Autowired
    private INightAuditService service;

    // Every day at 2 AM
//    @Scheduled(cron = "0 0 2 * * *")
//	@Scheduled(cron = "0 32 14 * * *", zone = "Asia/Kolkata")
//	@Scheduled(cron = "0 * * * * *")  // every minute
	@Scheduled(cron = "0 ${scheduler.minute} ${scheduler.hour} * * *",zone = "Asia/Kolkata")
    public void executeNightAudit() {
System.out.println("Night Audit Scheduler Started");
        log.info(
                "Night Audit Scheduler Started");

        service.runNightAudit();
    }
}