/**
 * 
 */
package com.pms.nightaudit.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.hotel.entity.Hotel;
import com.pms.hotel.repository.HotelRepository;
import com.pms.nightaudit.entity.BusinessDate;
import com.pms.nightaudit.entity.BusinessDateRequest;
import com.pms.nightaudit.repository.BusinessDateRepository;
import com.pms.nightaudit.service.IBusinessDateService;
import com.pms.security.configuration.HotelContext;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BusinessDateServiceImpl implements IBusinessDateService {

	@Autowired
	private BusinessDateRepository repository;

	@Autowired
	private HotelRepository hotelRepository;

	@Override
	public LocalDate getBusinessDate() {

		return repository.findTopByHotelIdAndIsActiveTrueAndIsDeletedFalse(HotelContext.getHotelId())
				.orElseThrow(
						() -> new RuntimeException("BusinessDate not found for hotelId: " + HotelContext.getHotelId()))
				.getBusinessDate();
	}

	@Override
	@Transactional
	public void moveToNextDay() {

		Long hotelId = HotelContext.getHotelId();

		BusinessDate currentBusinessDate = repository.findByHotelIdAndCurrentBusinessDateTrueAndIsActiveTrueAndIsDeletedFalse(hotelId)
				.orElseThrow(() -> new RuntimeException("Current business date not found"));

		// Close current business date
		currentBusinessDate.setCurrentBusinessDate(false);
		currentBusinessDate.setLastAuditAt(LocalDateTime.now());

		repository.save(currentBusinessDate);

		// Create next business date
		BusinessDate nextBusinessDate = new BusinessDate();

		nextBusinessDate.setHotel(currentBusinessDate.getHotel());

		nextBusinessDate.setBusinessDate(currentBusinessDate.getBusinessDate().plusDays(1));

		nextBusinessDate.setCurrentBusinessDate(true);

		nextBusinessDate.setAuditRunning(false);

		repository.save(nextBusinessDate);
	}

	// this method is created by twinkle if businessdate not found during night
	// audit. BUT currently on create hotel we are
	// creating one entry in businessdate entity
	@Override
	public BusinessDate addBusinessDate(BusinessDateRequest request) {
		Hotel hotel = hotelRepository.findById(request.getHotelId())
				.orElseThrow(() -> new RuntimeException("Hotel not found"));

		Optional<BusinessDate> existing = repository.findByHotelId(request.getHotelId());

		if (existing.isPresent()) {
			throw new RuntimeException("Business Date already exists for this hotel");
		}

		BusinessDate businessDate = new BusinessDate();
		businessDate.setHotelId(hotel.getId());
		businessDate.setBusinessDate(request.getBusinessDate());
		businessDate.setAuditRunning(false);
		businessDate.setCurrentBusinessDate(true);
		return repository.save(businessDate);
	}

}