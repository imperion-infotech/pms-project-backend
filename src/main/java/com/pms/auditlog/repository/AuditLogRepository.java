/**
 * 
 */
package com.pms.auditlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.auditlog.entity.AuditLog;

/**
 * 
 */
@Repository
public interface AuditLogRepository  extends JpaRepository<AuditLog, Long> {

}
