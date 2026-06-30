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
import com.pms.nightaudit.service.IAutoCheckoutService;
import com.pms.security.configuration.HotelContext;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AutoCheckoutServiceImpl
        implements IAutoCheckoutService {

    private final GuestDetailsRepository repository;

    @Override
    public void processDueOutGuests(
            LocalDate businessDate) {

        Long hotelId =
                HotelContext.getHotelId();

        List<GuestDetails> guests =
                repository.findDueOutGuests(
                        hotelId,
                        businessDate);

        for (GuestDetails guest : guests) {

            guest.setGuestDetailsStatus(
                    "CHECK_OUT");

            guest.setUpdatedOn(
                    LocalDateTime.now());

            repository.save(guest);

            log.info(
                    "Auto Checkout Completed : {}",
                    guest.getId());
        }
    }
}