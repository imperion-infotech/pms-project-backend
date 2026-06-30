/**
 * 
 */
package com.pms.nightaudit.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dao.impl.GuestDetailsRepository;
import com.pms.nightaudit.service.INoShowService;
import com.pms.security.configuration.HotelContext;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NoShowServiceImpl implements INoShowService {

	private final GuestDetailsRepository repository;

	@Override
	public void processNoShows(LocalDate businessDate) {

		Long hotelId = HotelContext.getHotelId();

		List<GuestDetails> guests = repository.findNoShowGuests(hotelId);

		for (GuestDetails guest : guests) {

			guest.setGuestDetailsStatus("NO_SHOW");

			guest.setUpdatedOn(LocalDateTime.now());

			repository.save(guest);

			log.info("No Show Processed : {}", guest.getId());
		}
	}
}