/**
 * 
 */
package com.pms.nightaudit.repository;

import com.pms.nightaudit.entity.NightAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NightAuditLogRepository
        extends JpaRepository<NightAuditLog, Long> {
}