package com.pms.policy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.policy.entity.PolicyDocument;

public interface PolicyDocumentRepository extends JpaRepository<PolicyDocument, Long> {

	List<PolicyDocument> findByTermsConditionId(Long termsConditionId);
}