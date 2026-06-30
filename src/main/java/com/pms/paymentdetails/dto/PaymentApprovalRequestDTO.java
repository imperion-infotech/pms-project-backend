/**
 * 
 */
package com.pms.paymentdetails.dto;

/**
 * 
 */

import com.pms.paymentdetails.enums.ApprovalType;
import com.pms.paymentdetails.enums.PaymentActionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentApprovalRequestDTO {

    private Long paymentId;

    private Double newAmount;

    private String newPaymentMethod;

    private PaymentActionType actionType;
    
    private Long paymentTypeId;

    /*
     * MANAGER APPROVAL
     */
    private String approverUsername;

    private String approverPassword;

    private String approvalPin;

    private ApprovalType approvalType;

    /*
     * APPROVAL REMARKS
     */
    private String remarks;
}