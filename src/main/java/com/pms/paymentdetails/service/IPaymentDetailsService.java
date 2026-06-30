/**
 * 
 */
package com.pms.paymentdetails.service;

import java.util.List;

import com.pms.paymentdetails.dto.AddPaymentApprovalRequestDTO;
import com.pms.paymentdetails.dto.PaymentApprovalRequestDTO;
import com.pms.paymentdetails.entity.PaymentDetails;
import com.pms.paymentdetails.entity.PaymentDetailsResponseDTO;

/**
 * 
 */
public interface IPaymentDetailsService {
	
	List<PaymentDetails> getAllPaymentDetails();
	PaymentDetails createPaymentDetails(PaymentDetails paymentDetails);
	PaymentDetails updatePaymentDetails(Long PaymentDetailsId, PaymentDetails paymentDetails);
	PaymentDetails getPaymentDetailsById(Long id);
	public boolean deletePaymentDetails(Long paymentDetailsId);
    public List<PaymentDetailsResponseDTO> getPaymentsByGuestId(Long guestId);
    public String getReceiptNo();
    PaymentDetails updatePaymentWithApproval(PaymentApprovalRequestDTO request);
//    void deletePaymentWithApproval(PaymentApprovalRequestDTO request);
    PaymentDetails addPaymentWithApproval(AddPaymentApprovalRequestDTO request);

}
