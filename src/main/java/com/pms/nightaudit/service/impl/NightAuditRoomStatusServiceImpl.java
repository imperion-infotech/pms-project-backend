/**
 * 
 */
package com.pms.nightaudit.service.impl;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dao.impl.GuestDetailsRepository;
import com.pms.nightaudit.service.INightAuditRoomStatusService;
import com.pms.room.dao.impl.RoomMasterRepository;
import com.pms.room.entity.RoomMaster;
import com.pms.roomstatus.dao.RoomStatusRepository;
import com.pms.roomstatus.entity.RoomStatus;
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
public class NightAuditRoomStatusServiceImpl
        implements INightAuditRoomStatusService {
	
	private static final Logger logger = LoggerFactory.getLogger(NightAuditRoomStatusServiceImpl.class);
	
    private final RoomMasterRepository roomRepository;

    private final GuestDetailsRepository guestRepository;
    
    private final RoomStatusRepository roomStatusRepository;

    @Override
    public void updateRoomStatuses() {

        Long hotelId =
                HotelContext.getHotelId();

        List<GuestDetails> guests =
                guestRepository.findCheckOutGuests(hotelId);

        for (GuestDetails guest : guests) {

            RoomMaster room =
                    roomRepository
                    .findByIdAndIsDeletedFalseAndHotelId(Long.valueOf(guest.getRoomMasterId()),hotelId);

            if (room == null) {
                continue;
            }

//            room.setRoomStatus("DIRTY");
            
         // Step 1: Fetch RoomStatus from DB by status name
            RoomStatus dirtyStatus = roomStatusRepository.findByRoomStatusNameAndHotelId("Vacant Dirty",guest.getHotelId());
                //.orElseThrow(() -> new RuntimeException("RoomStatus DIRTY not found"));
            		
            if(dirtyStatus == null) {
            	new RuntimeException("RoomStatus Vacant Dirty not found");
            }

            // Step 2: Set it on the room
            room.setRoomStatus(dirtyStatus);

            roomRepository.save(room);

            log.info(
                    "Room Marked DIRTY : {}",
                    room.getId());
        }
    }
}