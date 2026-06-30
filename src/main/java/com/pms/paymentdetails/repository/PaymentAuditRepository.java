package com.pms.paymentdetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.paymentdetails.entity.PaymentAudit;

public interface PaymentAuditRepository extends JpaRepository<PaymentAudit, Long> {
	
	

}
