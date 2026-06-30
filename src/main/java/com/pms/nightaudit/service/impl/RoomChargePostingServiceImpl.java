/**
 * 
 */
package com.pms.nightaudit.service.impl;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pms.building.controller.BuildingController;
import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dao.impl.GuestDetailsRepository;
import com.pms.nightaudit.entity.FolioTransaction;
import com.pms.nightaudit.repository.FolioTransactionRepository;
import com.pms.nightaudit.service.IRoomChargePostingService;
import com.pms.rent.RentDetails;
import com.pms.rent.dao.impl.RentDetailsRepository;
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
public class RoomChargePostingServiceImpl
        implements IRoomChargePostingService {
	
	private static final Logger logger = LoggerFactory.getLogger(RoomChargePostingServiceImpl.class);

    private final GuestDetailsRepository guestRepository;

    private final RentDetailsRepository rentRepository;

    private final FolioTransactionRepository folioRepository;

    @Override
    public Double postDailyRoomCharges() {

        Long hotelId =
                HotelContext.getHotelId();

        LocalDate businessDate =
                LocalDate.now();

        List<GuestDetails> guests =
                guestRepository
                .findCheckInGuests(
                        hotelId);

        double totalRevenue = 0;

        for (GuestDetails guest : guests) {

            boolean alreadyPosted =
                    folioRepository
                    .existsByGuestIdAndBusinessDateAndTransactionTypeAndIsDeletedFalse(
                            guest.getId(),
                            businessDate,
                            "ROOM_RENT");

            if (alreadyPosted) {
                continue;
            }

            RentDetails rent =
                    rentRepository
                    .findByIdAndIsDeletedFalse(
                            Long.valueOf(
                                    guest.getRentDetailsId()))
                    .orElse(null);

            if (rent == null) {
                continue;
            }

            Double amount =
                    rent.getRent();

            FolioTransaction transaction =
                    new FolioTransaction();

            transaction.setHotelId(hotelId);

            transaction.setGuestId(
                    guest.getId());

            transaction.setRentDetailsId(
                    rent.getId());

            transaction.setBusinessDate(
                    businessDate);

            transaction.setTransactionType(
                    "ROOM_RENT");

            transaction.setAmount(amount);

            transaction.setRemarks(
                    "Night Audit Room Posting");

            transaction.setCreatedOn(
                    LocalDateTime.now());

            folioRepository.save(transaction);

            rent.setBalance(
                    rent.getBalance() + amount);

            rent.setTotalRental(
                    rent.getTotalRental() + amount);

            rentRepository.save(rent);

            totalRevenue += amount;

            log.info(
                    "Room Charge Posted : {}",
                    guest.getId());
        }

        return totalRevenue;
    }
}