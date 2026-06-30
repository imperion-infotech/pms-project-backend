/**
 * 
 */
package com.pms.paymenttype.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.paymenttype.entity.PaymentType;

/**
 * 
 */
@Repository
public interface PaymentTypeRepository  extends SoftDeleteRepository<PaymentType, Integer> , JpaSpecificationExecutor<PaymentType>{
	
List<PaymentType> findByHotelId(Long hotelId);
	
	PaymentType findByIdAndHotelId(Long paymentTypeId,Long hotelId);
	
	List<PaymentType> findByIsDeletedFalseAndHotelId(Long hotelId);
	List<PaymentType> findByIsDeletedFalse();
	
	
	@Query("SELECT p FROM PaymentType p WHERE p.hotelId = :hotelId and p.isDeleted=:isDeleted and p.isActive=:isActive")
	List<PaymentType> findPaymentTypes(@Param("hotelId") Long hotelId,@Param("isDeleted") Boolean isDeleted, @Param("isActive") Boolean isActive);

	Optional<PaymentType> findByPaymentTypeNameAndIsDeletedFalseAndHotelId(String paymentTypeName,Long hotelId);
	
}
