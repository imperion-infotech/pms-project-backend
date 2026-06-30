/**
 * 
 */
package com.pms.othercharge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.othercharge.entity.OtherCharge;
import com.pms.othercharge.entity.OtherChargeDetails;
import com.pms.paymentdetails.entity.PaymentDetails;

/**
 * 
 */
public interface OtherChargeDetailsRepository extends SoftDeleteRepository<OtherChargeDetails, Long>,JpaSpecificationExecutor<OtherChargeDetails>{
	
	List<OtherChargeDetails> findByHotelId(Long hotelId);
	
	OtherChargeDetails findByIdAndHotelId(Long otherChargeDetailsId,Long hotelId);
	
	@Query("SELECT o FROM OtherChargeDetails o WHERE o.hotelId = :hotelId")
	List<OtherCharge> findOtherChargeDetails(@Param("hotelId") Long hotelId);
	
	List<OtherChargeDetails> findByGuestDetailsIdAndHotelId(Long guestId,Long hotelId);
	
	@Query("""
		       SELECT o
		       FROM OtherChargeDetails o
		       WHERE o.guestDetails.id = :guestId
		       AND o.hotelId = :hotelId
		       """)
		List<OtherChargeDetails> findCharges(
		        @Param("guestId") Long guestId,
		        @Param("hotelId") Long hotelId);

}
