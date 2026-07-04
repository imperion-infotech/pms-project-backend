/**
 * 
 */
package com.pms.paymentdetails.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pms.auditlog.annotation.Auditable;
import com.pms.auditlog.context.BusinessTraceContext;
import com.pms.auditlog.context.RequestTraceContext;
import com.pms.auditlog.entity.AuditLog;
import com.pms.auditlog.repository.AuditLogRepository;
import com.pms.auditlog.util.AuditUtil;
import com.pms.common.service.SoftDeleteService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.paymentdetails.dto.AddPaymentApprovalRequestDTO;
import com.pms.paymentdetails.dto.PaymentApprovalRequestDTO;
import com.pms.paymentdetails.entity.PaymentAudit;
import com.pms.paymentdetails.entity.PaymentDetails;
import com.pms.paymentdetails.entity.PaymentDetailsResponseDTO;
import com.pms.paymentdetails.enums.ApprovalType;
import com.pms.paymentdetails.enums.PaymentActionType;
import com.pms.paymentdetails.repository.PaymentAuditRepository;
import com.pms.paymentdetails.repository.PaymentDetailsRepository;
import com.pms.paymentdetails.service.IPaymentDetailsService;
import com.pms.paymenttype.entity.PaymentType;
import com.pms.paymenttype.repository.PaymentTypeRepository;
import com.pms.paymenttype.service.IPaymentTypeService;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.entity.User;
import com.pms.security.repository.UserRepository;
import com.pms.security.service.AuthService;
import com.pms.security.service.BaseHotelService;

/**
 * 
 */

@Service
public class PaymentDetailsServiceImpl extends BaseHotelService implements IPaymentDetailsService {

	static final Logger logger = LoggerFactory.getLogger(PaymentDetailsServiceImpl.class);

	@Autowired
	private PaymentDetailsRepository paymentDetailsRepository;

	@Autowired
	private PaymentTypeRepository paymentTypeRepository;

	@Autowired
	private IPaymentTypeService service;

	@Autowired
	private SoftDeleteService softDeleteService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PaymentAuditRepository paymentAuditRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthService authService;

	@Autowired
	private AuditLogRepository auditLogRepository;

	public PaymentDetailsServiceImpl(PaymentDetailsRepository paymentDetailsRepository,
			PaymentTypeRepository paymentTypeRepository, IPaymentTypeService service,
			SoftDeleteService softDeleteService, UserRepository userRepository,
			PaymentAuditRepository paymentAuditRepository, PasswordEncoder passwordEncoder) {
		super();
		this.paymentDetailsRepository = paymentDetailsRepository;
		this.paymentTypeRepository = paymentTypeRepository;
		this.service = service;
		this.softDeleteService = softDeleteService;
		this.userRepository = userRepository;
		this.paymentAuditRepository = paymentAuditRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<PaymentDetails> getAllPaymentDetails() {
		Long hotelId = HotelContext.getHotelId();

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

		validateHotelAccess(hotelId);
		if (isSuperAdmin())
			return paymentDetailsRepository.findByIsDeletedFalse();
		else
			return paymentDetailsRepository.findByIsDeletedFalseAndHotelId(HotelContext.getHotelId());
	}

	@Override
	public PaymentDetails getPaymentDetailsById(Long id) {
		Long hotelId = HotelContext.getHotelId();

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

		PaymentDetails details = paymentDetailsRepository.findByIdAndHotelId(id, hotelId);

		PaymentType type = service
				.getPaymentTypeById(details.getPaymentType() != null ? details.getPaymentType().getId() : null);
		if (type != null) {
			details.setPaymentType(type);
		}
		return details;
	}

	@Auditable(action = "CREATE", entity = "PAYMENTDETAILS")
	@Override
	public PaymentDetails createPaymentDetails(PaymentDetails paymentDetails) {
		paymentDetails.setPaymentType(paymentDetails.getPaymentType());
		Long userId = UserContext.getUserId();

		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		assignHotel(paymentDetails, paymentDetails.getHotelId());
		PaymentType paymentType = paymentTypeRepository
		        .findByIdAndHotelId(
		                paymentDetails.getPaymentType().getId(),
		                HotelContext.getHotelId());

		if (paymentType == null) {
		    throw new RuntimeException("Payment type not found");
		}
		paymentDetails.setPaymentType(paymentType);
		paymentDetails.setCreatedBy(userId);
		PaymentDetails b=  paymentDetailsRepository.save(paymentDetails);
		
		
		
		/*
		 * SAVE AUDIT
		 */
		PaymentAudit audit = new PaymentAudit();

		audit.setPaymentId(b.getId());
		
		audit.setHotelId(paymentDetails.getHotelId());

		audit.setActionType(PaymentActionType.PAYMENT_DETAILS_CREATE);

		audit.setOldAmount(null);

		audit.setNewAmount(paymentDetails.getAmount());

		audit.setOldPaymentMethod(null);

		audit.setNewPaymentMethod(paymentDetails.getPaymentType().getPaymentTypeName());

		audit.setRemarks(paymentDetails.getRemark());

		audit.setUpdatedBy(authService.getCurrentUser().getId());

 		audit.setApprovedBy(authService.getCurrentUser().getId());
 		
 		audit.setIsRefund(paymentDetails.getIsRefund());
 		audit.setRefundAmount(paymentDetails.getRefundAmount());
 		audit.setRefundType(paymentDetails.getRefundType());
 		audit.setRefundAccountNo(paymentDetails.getRefundAccountNo());
 		audit.setTransactionId(paymentDetails.getTransactionId());
        audit.setCreatedOn(LocalDateTime.now());
        
        if(paymentDetails.getGuestDetails() != null)
        	audit.setGuestDetailsId(paymentDetails.getGuestDetails().getId());

		paymentAuditRepository.save(audit);
		
		
		
		return b;
	}

	@Override
	public PaymentDetails updatePaymentDetails(Long paymentDetailsId, PaymentDetails paymentDetails) {
		PaymentDetails paymentDetailsFromDB = getPaymentDetailsById(paymentDetailsId);
		paymentDetailsFromDB.setAuthorizationNo(paymentDetails.getAuthorizationNo());
		paymentDetailsFromDB.setAmount(paymentDetails.getAmount());
		paymentDetailsFromDB.setCardNo(paymentDetails.getCardNo());
		paymentDetailsFromDB.setCurrencySymbol(paymentDetails.getCurrencySymbol());
		paymentDetailsFromDB.setPaymentType(paymentDetails.getPaymentType());
//		paymentDetailsFromDB.setPaymentTypesId(paymentDetails.getPaymentTypesId());
		paymentDetailsFromDB.setReceiptNo(paymentDetails.getReceiptNo());
		paymentDetailsFromDB.setRemark(paymentDetails.getRemark());
		paymentDetailsFromDB.setValidTill(paymentDetails.getValidTill());
		paymentDetailsFromDB.setPaymentDate(paymentDetails.getPaymentDate());
		paymentDetailsFromDB.setTotalAmount(paymentDetails.getTotalAmount());
		paymentDetailsFromDB.setIsRefund(paymentDetails.getIsRefund());
		paymentDetailsFromDB.setRefundAmount(paymentDetails.getRefundAmount());
		paymentDetailsFromDB.setRefundType(paymentDetails.getRefundType());
		paymentDetailsFromDB.setRefundAccountNo(paymentDetails.getRefundAccountNo());
		paymentDetailsFromDB.setTransactionId(paymentDetails.getTransactionId());
		

		Long userId = UserContext.getUserId();
		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		paymentDetailsFromDB.setUpdatedBy(userId);
		paymentDetailsFromDB.setUpdatedOn(LocalDateTime.now());

		paymentDetailsRepository.save(paymentDetailsFromDB);
		PaymentDetails updatedPaymentDetails = getPaymentDetailsById(paymentDetailsId);
		return updatedPaymentDetails;
	}

	@Auditable(action = "DELETE", entity = "PAYMENTDETAILS")
	@Override
	public boolean deletePaymentDetails(Long paymentDetailsId) {
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
		
		PaymentDetails b = softDeleteService.softDelete(paymentDetailsId, paymentDetailsRepository);
		
		PaymentAudit audit = new PaymentAudit();

		audit.setPaymentId(b.getId());

		audit.setActionType(PaymentActionType.PAYMENT_DETAILS_DELETE);

		audit.setOldAmount(b.getAmount());

		audit.setNewAmount(0.0);

		audit.setOldPaymentMethod(b.getPaymentType().getPaymentTypeName());

		audit.setNewPaymentMethod(null);

		audit.setRemarks(b.getRemark());

		audit.setUpdatedBy(authService.getCurrentUser().getId());

		audit.setApprovedBy(authService.getCurrentUser().getId());

		audit.setCreatedOn(LocalDateTime.now());
		
		audit.setIsRefund(b.getIsRefund());
 		audit.setRefundAmount(b.getRefundAmount());
 		audit.setRefundType(b.getRefundType());
 		audit.setRefundAccountNo(b.getRefundAccountNo());
 		audit.setTransactionId(b.getTransactionId());
 		audit.setGuestDetailsId( b.getGuestDetails() != null ? b.getGuestDetails().getId() : 0);
 		
		paymentAuditRepository.save(audit);
		
		return b == null ? false : true;
	}

	public List<PaymentDetailsResponseDTO> getPaymentsByGuestId(Long guestId) {

		Long hotelId = HotelContext.getHotelId();

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

		List<PaymentDetails> payments = paymentDetailsRepository.findByGuestDetailsIdAndHotelId(guestId, hotelId);

		return payments.stream().map(p -> {
			PaymentDetailsResponseDTO dto = new PaymentDetailsResponseDTO();

			dto.setId(p.getId());
			dto.setAmount(p.getAmount());
			dto.setTotalAmount(p.getTotalAmount());
//	                    dto.setPaymentMode(p.getPaymentTypes().get(0).getPaymentTypeName());
			if (p.getPaymentType() != null) {
				dto.setPaymentMode(p.getPaymentType().getPaymentTypeName());
			}
			dto.setReceiptNumber(p.getReceiptNo());
			dto.setRemark(p.getRemark());
			dto.setPaymentDate(p.getPaymentDate());
			dto.setGuestDetailsId(p.getGuestDetails().getId());
			dto.setIsActive(p.getIsActive());
			dto.setIsDeleted(p.getIsDeleted());
			dto.setIsRefund(p.getIsRefund());
	 		dto.setRefundAmount(p.getRefundAmount());
	 		dto.setRefundType(p.getRefundType());
	 		dto.setRefundAccountNo(p.getRefundAccountNo());
	 		dto.setTransactionId(p.getTransactionId());
	 		if(p.getPaymentType() != null) {
	 			dto.setPaymentType(p.getPaymentType().getPaymentTypeName());
	 		}
	 		
	 		 if(p.getGuestDetails() != null)
	         	dto.setGuestDetailsId(p.getGuestDetails().getId());
	 		
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public String getReceiptNo() {

		Random random = new Random();
		int number = 10000 + random.nextInt(90000);
		return String.valueOf(number);
	}

	@Auditable(action = "UPDATE", entity = "PAYMENTDETAILS")
	@Override
	public PaymentDetails updatePaymentWithApproval(PaymentApprovalRequestDTO request) {

		Long hotelId = HotelContext.getHotelId();
		validateHotelAccess(hotelId);
		
		/*
		 * GET MANAGER
		 */
		User manager = userRepository.findByUsernameWithRolesAndIsDeletedFalse(authService.getCurrentUser().getUsername())
				.orElseThrow(() -> new RuntimeException("Manager not found"));

		/*
		 * CHECK PERMISSION
		 */
		if(!isSuperAdmin()){
		boolean hasPermission = manager.getRoles().stream().flatMap(role -> role.getPermissions().stream())
				.anyMatch(permission -> permission.getName().equals("PAYMENT_DETAILS_UPDATE"));

		if (!hasPermission) {

			throw new RuntimeException("Manager approval permission denied");
		}
		}

		/*
		 * PASSWORD / PIN VALIDATION
		 */
		// FOLLOWING IS FOR PASSWORD AND PIN APPROVAL CODE RIGHT NOW IT IS COMMENTED
		// *****************************//
		/*
		 * boolean valid = false;
		 * 
		 * if (request.getApprovalType() == ApprovalType.PASSWORD) {
		 * 
		 * valid = passwordEncoder.matches(request.getApproverPassword(),
		 * manager.getPassword()); } else if (request.getApprovalType() ==
		 * ApprovalType.PIN) {
		 * 
		 * valid = passwordEncoder.matches(request.getApprovalPin(),
		 * manager.getApprovalPin()); }
		 * 
		 * if (!valid) {
		 * 
		 * throw new RuntimeException("Invalid approval credentials"); }
		 */
		// ******************************//

		/*
		 * GET PAYMENT
		 */
		PaymentDetails payment = paymentDetailsRepository.findById(request.getPaymentId())
				.orElseThrow(() -> new RuntimeException("Payment not found"));

		/*
		 * STORE OLD VALUES
		 */
		Double oldAmount = payment.getAmount();

		String oldPaymentType = payment.getPaymentType().getPaymentTypeName();

		/*
		 * UPDATE PAYMENT
		 */
		payment.setAmount(request.getNewAmount());

		PaymentType paymentType = paymentTypeRepository
				.findByPaymentTypeNameAndIsDeletedFalseAndHotelId(request.getNewPaymentMethod(),
						HotelContext.getHotelId())
				.orElseThrow(() -> new RuntimeException("Payment type not found"));

		payment.setPaymentType(paymentType);
		payment.setUpdatedBy(authService.getCurrentUser().getId());
		payment.setUpdatedOn(LocalDateTime.now());

		paymentDetailsRepository.save(payment);

		/*
		 * CURRENT USER
		 */
		User currentUser = authService.getCurrentUser();
		/*
		 * SAVE AUDIT LOG
		 */
		PaymentAudit audit = new PaymentAudit();

		audit.setPaymentId(payment.getId());

		audit.setActionType(request.getActionType());

		audit.setOldAmount(oldAmount);

		audit.setNewAmount(request.getNewAmount());

		audit.setOldPaymentMethod(oldPaymentType);

		audit.setNewPaymentMethod(request.getNewPaymentMethod());

		audit.setRemarks(request.getRemarks());

		audit.setUpdatedBy(currentUser.getId());

		audit.setApprovedBy(manager.getId());

		audit.setApprovalType(request.getApprovalType());

		audit.setCreatedOn(LocalDateTime.now());
		
		audit.setIsRefund(payment.getIsRefund());
 		audit.setRefundAmount(payment.getRefundAmount());
 		audit.setRefundType(payment.getRefundType());
 		
 		audit.setRefundAccountNo(payment.getRefundAccountNo());
 		audit.setTransactionId(payment.getTransactionId());
 		
 		audit.setGuestDetailsId(payment.getGuestDetails().getId());

		paymentAuditRepository.save(audit);

		return payment;
	}

	

	@Auditable(action = "CREATE", entity = "PAYMENTDETAILS")
	@Override
	public PaymentDetails addPaymentWithApproval(AddPaymentApprovalRequestDTO request) {

		/*
		 * GET MANAGER
		 */
		User manager = userRepository.findByUsernameWithRolesAndIsDeletedFalse(request.getApproverUsername())
				.orElseThrow(() -> new RuntimeException("Manager not found"));

		/*
		 * CHECK PERMISSION
		 */
		boolean hasPermission = manager.getRoles().stream().flatMap(role -> role.getPermissions().stream())
				.anyMatch(permission -> permission.getName().equals("PAYMENT_DETAILS_CREATE"));

		if (!hasPermission) {

			throw new RuntimeException("Manager approval permission denied");
		}

		/*
		 * PASSWORD / PIN VALIDATION
		 */
		// OPTIONAL FUTURE IMPLEMENTATION

		/*
		 * GET PAYMENT TYPE
		 */
		PaymentType paymentType = paymentTypeRepository
				.findByPaymentTypeNameAndIsDeletedFalseAndHotelId(request.getPaymentMethod(), HotelContext.getHotelId())
				.orElseThrow(() -> new RuntimeException("Payment type not found"));

		/*
		 * CURRENT USER
		 */
		User currentUser = authService.getCurrentUser();

		/*
		 * CREATE PAYMENT
		 */
		PaymentDetails payment = new PaymentDetails();

//		payment.setGuestId(request.getGuestDetailsId());

		payment.setAmount(request.getAmount());

		payment.setPaymentType(paymentType);

		payment.setRemark(request.getRemarks());

		payment.setCreatedBy(currentUser.getId());

		payment.setCreatedOn(LocalDateTime.now());

		payment.setUpdatedBy(currentUser.getId());

		payment.setUpdatedOn(LocalDateTime.now());
		
		payment.setPaymentDate(request.getPaymentDate());

		payment.setCurrencySymbol(request.getCurrencySymbol());
		payment.setReceiptNo(request.getReceiptNo());
		payment.setValidTill(request.getValidTill());
		payment.setCardNo(request.getCardNo());
		payment.setTotalAmount(request.getTotalAmount());
		
		payment.setIsRefund(request.getIsRefund());
		payment.setRefundAmount(request.getRefundAmount());
		payment.setRefundType(request.getRefundType());
		payment.setRefundAccountNo(request.getRefundAccountNo());
		payment.setTransactionId(request.getTransactionId());
		
		
		paymentDetailsRepository.save(payment);

		/*
		 * SAVE AUDIT
		 */
		PaymentAudit audit = new PaymentAudit();

		audit.setPaymentId(payment.getId());

		audit.setActionType(PaymentActionType.PAYMENT_DETAILS_CREATE);

		audit.setOldAmount(null);

		audit.setNewAmount(request.getAmount());

		audit.setOldPaymentMethod(null);

		audit.setNewPaymentMethod(request.getPaymentMethod());

		audit.setRemarks(request.getRemarks());

		audit.setUpdatedBy(currentUser.getId());

		audit.setApprovedBy(manager.getId());

		audit.setApprovalType(ApprovalType.PASSWORD);

		audit.setCreatedOn(LocalDateTime.now());

		paymentAuditRepository.save(audit);

		return payment;
	}

}
