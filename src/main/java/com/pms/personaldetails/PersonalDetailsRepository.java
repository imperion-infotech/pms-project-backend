package com.pms.personaldetails;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pms.building.entity.Building;
import com.pms.common.repository.SoftDeleteRepository;

public interface PersonalDetailsRepository extends SoftDeleteRepository<PersonalDetails, Long>, JpaSpecificationExecutor<PersonalDetails> {

	Optional<PersonalDetails> findByIdAndHotelIdAndIsDeletedFalse(Long id, Long hotelId);
	List<PersonalDetails> findByHotelIdAndIsDeletedFalse(Long hotelId);
	
	List<PersonalDetails> findByIsDeletedFalseAndHotelId(Long hotelId);
	List<PersonalDetails> findByIsDeletedFalse();
}
