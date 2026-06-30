/**
 * 
 */
package com.pms.paymentdetails.entity;

import com.pms.baseentity.BaseEntity;
import com.pms.paymentdetails.enums.ApprovalType;
import com.pms.paymentdetails.enums.PaymentActionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment_audit")
@Getter
@Setter
public class PaymentAudit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private PaymentActionType actionType;

    @Column(name = "old_amount")
    private Double oldAmount;

    @Column(name = "new_amount")
    private Double newAmount;

    @Column(name = "old_payment_method")
    private String oldPaymentMethod;

    @Column(name = "new_payment_method")
    private String newPaymentMethod;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_type")
    private ApprovalType approvalType;
    
    private Boolean isRefund;
	
	private Double refundAmount;
	
	private String refundType;
	
	private String transactionId;
	
	private String refundAccountNo;
	
	private Long guestDetailsId;

}