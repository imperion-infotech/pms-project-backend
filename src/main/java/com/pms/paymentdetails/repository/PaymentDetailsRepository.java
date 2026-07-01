/**
 * 
 */
package com.pms.paymentdetails.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.paymentdetails.entity.PaymentDetails;

/**
 * 
 */
@Repository
public interface PaymentDetailsRepository extends SoftDeleteRepository<PaymentDetails, Long> , JpaSpecificationExecutor<PaymentDetails>{
	
	List<PaymentDetails> findByGuestDetailsIdAndHotelId(Long guestId,Long hotelId);
	
	List<PaymentDetails> findByHotelId(Long hotelId);
	
	PaymentDetails findByIdAndHotelId(Long paymentDetailsId,Long hotelId);
	
	List<PaymentDetails> findByIsDeletedFalseAndHotelId(Long hotelId);
	List<PaymentDetails> findByIsDeletedFalse();
	
	 Optional<PaymentDetails> findByIdAndIsDeletedFalse(Long id);
	 
	 PaymentDetails findByIdAndHotelIdAndIsDeletedFalse(Long paymentDetailsId,Long hotelId);
	
	@Query("SELECT p FROM PaymentDetails p WHERE p.hotelId = :hotelId")
	List<PaymentDetails> findPaymentDetails(@Param("hotelId") Long hotelId);
	
	List<PaymentDetails> findByGuestDetailsId(Long guestId);
	
	

}
