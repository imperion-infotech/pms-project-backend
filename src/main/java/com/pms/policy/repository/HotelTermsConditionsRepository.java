/**
 * 
 */
package com.pms.policy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.paymentdetails.entity.PaymentDetails;
import com.pms.policy.entity.HotelTermsConditions;
import com.pms.policy.enums.PolicyType;

public interface HotelTermsConditionsRepository extends SoftDeleteRepository<HotelTermsConditions, Long> , JpaRepository<HotelTermsConditions, Long> {

	Page<HotelTermsConditions> findByHotelIdAndIsDeletedFalse(Long hotelId, Pageable pageable);

	Page<HotelTermsConditions> findByHotelIdAndTitleContainingIgnoreCaseAndIsDeletedFalse(Long hotelId, String keyword,
			Pageable pageable);

	HotelTermsConditions findByTermsConditionIdAndIsDeletedFalse(Long id);

	Page<HotelTermsConditions> findByHotelIdAndPolicyTypeAndIsDeletedFalse(Long hotelId, PolicyType policyType,
			Pageable pageable);
	
	Page<HotelTermsConditions> findByHotelIdAndPolicyType(Long hotelId, PolicyType policyType,
			Pageable pageable);
}