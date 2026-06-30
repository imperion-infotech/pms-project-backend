/**
 * 
 */
package com.pms.nightaudit.service.impl;

import org.springframework.stereotype.Service;

import com.pms.guestdetails.dao.impl.GuestDetailsRepository;
import com.pms.nightaudit.service.IOccupancyReportService;
import com.pms.security.configuration.HotelContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OccupancyReportServiceImpl
        implements IOccupancyReportService {

    private final GuestDetailsRepository repository;

    @Override
    public Long getOccupiedRooms() {

        return repository
                .countByHotelIdAndGuestDetailsStatusAndIsDeletedFalse(
                        HotelContext.getHotelId(),
                        "CHECKED_IN");
    }
}