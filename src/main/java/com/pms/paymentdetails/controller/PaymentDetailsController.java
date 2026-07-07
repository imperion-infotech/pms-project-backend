/**
 * 
 */
package com.pms.paymentdetails.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pms.paymentdetails.dto.AddPaymentApprovalRequestDTO;
import com.pms.paymentdetails.dto.PaymentApprovalRequestDTO;
import com.pms.paymentdetails.entity.PaymentDetails;
import com.pms.paymentdetails.entity.PaymentDetailsResponseDTO;
import com.pms.paymentdetails.service.IPaymentDetailsService;

/**
 * 
 */
@RestController
public class PaymentDetailsController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentDetailsController.class);

	@Autowired
	private IPaymentDetailsService service;

//	@PreAuthorize("hasAuthority('PAYMENT_DETAILS_VIEW')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'PAYMENT_DETAILS_VIEW')")
	@GetMapping("/user/getAllpaymentdetails")
	public ResponseEntity<List<PaymentDetails>> getPaymentDetails() {
		List<PaymentDetails> paymentDetails = service.getAllPaymentDetails();
		return new ResponseEntity<List<PaymentDetails>>(paymentDetails, HttpStatus.OK);
	}

//	@PreAuthorize("hasAuthority('PAYMENT_DETAILS_VIEW')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'PAYMENT_DETAILS_VIEW')")
	@GetMapping("/user/getpaymentdetails/{id}")
	public ResponseEntity<PaymentDetails> getPaymentDetails(@PathVariable("id") Long id) {
		PaymentDetails paymentDetails = service.getPaymentDetailsById(id);
		return new ResponseEntity<PaymentDetails>(paymentDetails, HttpStatus.OK);
	}

//	@PreAuthorize("hasAuthority('PAYMENT_DETAILS_CREATE')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'PAYMENT_DETAILS_CREATE')")
	@PostMapping("/admin/createpaymentdetails")
	public ResponseEntity<?> createPaymentDetails(@RequestBody PaymentDetails paymentDetails) {
		// Validate input
//		if (paymentDetails == null || paymentDetails.getPaymentType()== null ) {
//			return ResponseEntity.badRequest().body("PaymentDetails PaymentType must not be null or empty");
//		}

		if (paymentDetails == null || paymentDetails.getCurrencySymbol() == null
				|| paymentDetails.getCurrencySymbol().toString().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("PaymentDetails CurrencySymbol must not be null or empty");
		}

		if (paymentDetails == null || paymentDetails.getPaymentDate() == null) {
			return ResponseEntity.badRequest().body("PaymentDetails payment date must not be null or empty");
		}

//		if (paymentDetails == null || paymentDetails.getGuestDetails() == null) {
//			return ResponseEntity.badRequest().body("PaymentDetails guestdetails id must not be null or empty");
//		}

		try {
			
			PaymentDetails savedPaymentDetails = service.createPaymentDetails(paymentDetails);
			return ResponseEntity.ok(savedPaymentDetails);
		} catch (Exception e) {
			logger.error("Exception in controller of createpaymentdetails api :" + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the PaymentDetails");
		}
	}

//	@PreAuthorize("hasAuthority('PAYMENT_DETAILS_UPDATE')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'PAYMENT_DETAILS_UPDATE')")
	@PutMapping("/admin/updatepaymentdetails/{id}")
	public ResponseEntity<?> updatePaymentDetails(@PathVariable Long id, @RequestBody PaymentDetails paymentDetails) {
		// Validate input
		if (paymentDetails == null || paymentDetails.getPaymentType() == null) {
			return ResponseEntity.badRequest().body("PaymentDetails PaymentType must not be null or empty");
		}

		if (paymentDetails == null || paymentDetails.getCurrencySymbol() == null
				|| paymentDetails.getCurrencySymbol().toString().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("PaymentDetails CurrencySymbol must not be null or empty");
		}

		try {
			PaymentDetails existingPaymentDetails = service.getPaymentDetailsById(id);
			if (existingPaymentDetails == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" with ID " + id + " not found");
			}

			// Update fields
			existingPaymentDetails.setAmount(paymentDetails.getAmount());
			existingPaymentDetails.setTotalAmount(paymentDetails.getTotalAmount());
			existingPaymentDetails.setAuthorizationNo(paymentDetails.getAuthorizationNo());
			existingPaymentDetails.setCardNo(paymentDetails.getCardNo());
			existingPaymentDetails.setCurrencySymbol(paymentDetails.getCurrencySymbol());
			existingPaymentDetails.setPaymentType(paymentDetails.getPaymentType());
			existingPaymentDetails.setReceiptNo(paymentDetails.getReceiptNo());
			existingPaymentDetails.setRemark(paymentDetails.getRemark());
			existingPaymentDetails.setValidTill(paymentDetails.getValidTill());
			existingPaymentDetails.setGuestDetails(paymentDetails.getGuestDetails());
			existingPaymentDetails.setIsRefund(paymentDetails.getIsRefund());
			existingPaymentDetails.setRefundAmount(paymentDetails.getRefundAmount());
			existingPaymentDetails.setRefundType(paymentDetails.getRefundType());
			existingPaymentDetails.setRefundAccountNo(paymentDetails.getRefundAccountNo());
			existingPaymentDetails.setTransactionId(paymentDetails.getTransactionId());
			existingPaymentDetails.setIsActive(paymentDetails.getIsActive());
			existingPaymentDetails.setIsDeleted(paymentDetails.getIsDeleted());

			PaymentDetails updatedPaymentDetails = service.updatePaymentDetails(existingPaymentDetails.getId(),
					existingPaymentDetails);
			return ResponseEntity.ok(updatedPaymentDetails);

		} catch (Exception e) {
			logger.error("Exception in controller of updatepaymentdetails api :" + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the PaymentDetails");
		}
	}

//	@PreAuthorize("hasAuthority('PAYMENT_DETAILS_DELETE')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'PAYMENT_DETAILS_DELETE')")
	@DeleteMapping("/admin/deletepaymentdetails/{id}")
	public ResponseEntity<String> deletePaymentDetails(@PathVariable("id") Long id) {
		boolean isDeleted = service.deletePaymentDetails(id);
		if (isDeleted) {
			String responseContent = "PaymentDetails has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting PaymentDetails from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@PreAuthorize("hasAuthority('PAYMENT_DETAILS_VIEW')")
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'PAYMENT_DETAILS_VIEW')")
	@GetMapping("/user/guest/{guestId}")
	public ResponseEntity<List<PaymentDetailsResponseDTO>> getPaymentsByGuest(@PathVariable Long guestId) {

		List<PaymentDetailsResponseDTO> response = service.getPaymentsByGuestId(guestId);

		return ResponseEntity.ok(response);
	}

//	@PreAuthorize("hasAuthority('PAYMENT_DETAILS_GET_RECEIPT_NO')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'PAYMENT_DETAILS_GET_RECEIPT_NO')")
	@GetMapping("/user/getreceiptno")
	public ResponseEntity<String> getReceiptNo() {
		return new ResponseEntity<String>(service.getReceiptNo(), HttpStatus.OK);
	}

//	@PreAuthorize("hasAuthority('PAYMENT_DETAILS_UPDATE')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'PAYMENT_DETAILS_UPDATE')")
//	@PostMapping("/admin/update-payment-details")
	public ResponseEntity<?> updatePayment(@RequestBody PaymentApprovalRequestDTO request) {

		return ResponseEntity.ok(service.updatePaymentWithApproval(request));
	}

//	@PreAuthorize("hasAuthority('PAYMENT_DETAILS_DELETE')")
//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'PAYMENT_DETAILS_DELETE')")
//	@PostMapping("/delete-payment-details")
	public ResponseEntity<?> deletePayment(@RequestBody PaymentApprovalRequestDTO request) {

//		service.deletePaymentWithApproval(request);

		return ResponseEntity.ok("Payment deleted successfully");
	}
	
//	@PostMapping("/add-payment-with-approval")
//	public ResponseEntity<PaymentDetails> addPaymentWithApproval(@RequestBody AddPaymentApprovalRequestDTO request) {
//
//	    return ResponseEntity.ok(service.addPaymentWithApproval(request));
//	}

}
