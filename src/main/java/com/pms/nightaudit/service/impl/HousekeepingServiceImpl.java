package com.pms.nightaudit.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.pms.nightaudit.entity.HousekeepingStatus;
import com.pms.nightaudit.repository.HousekeepingRepository;
import com.pms.nightaudit.service.IHousekeepingService;
import com.pms.security.configuration.HotelContext;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HousekeepingServiceImpl implements IHousekeepingService {

    private final HousekeepingRepository repository;

    @Override
    public void createDirtyRoomTask(
            Long roomId) {

        HousekeepingStatus hk =
                new HousekeepingStatus();

        hk.setRoomId(roomId);

        hk.setRoomStatus("DIRTY");

        hk.setHousekeepingStatus(
                "PENDING");

        hk.setBusinessDate(
                LocalDate.now());

        hk.setHotelId(
                HotelContext.getHotelId());

        hk.setCreatedOn(
                LocalDateTime.now());

        repository.save(hk);
    }
}