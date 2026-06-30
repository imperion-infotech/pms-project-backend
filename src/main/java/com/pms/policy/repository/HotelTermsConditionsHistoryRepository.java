/**
 * 
 */
package com.pms.policy.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.policy.entity.HotelTermsConditionsHistory;

public interface HotelTermsConditionsHistoryRepository extends JpaRepository<HotelTermsConditionsHistory, Long> {

	List<HotelTermsConditionsHistory> findByTermsConditionIdOrderByVersionNoDesc(Long termsConditionId);
}