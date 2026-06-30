/**
 * 
 */
package com.pms.auditlog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.pms.auditlog.entity.AuditLog;
import com.pms.auditlog.repository.AuditLogRepository;
import com.pms.building.service.impl.BuildingServiceImpl;

/**
 * 
 */
@Service
//@RequiredArgsConstructor
public class AuditService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

	private final AuditLogRepository auditLogRepository;
	
	public AuditService(AuditLogRepository auditLogRepository) {
		super();
		this.auditLogRepository = auditLogRepository;
	}

	@Async
	public void save(AuditLog log) {
		auditLogRepository.save(log);
	}
}
