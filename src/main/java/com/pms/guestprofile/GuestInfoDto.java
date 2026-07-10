/**
 * 
 */
package com.pms.guestprofile;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.pms.guestdetails.GuestDetails;

import lombok.Data;

/**
 * 
 */
@Data
public class GuestInfoDto {
    private Long roomMasterId;
    private Long personalDetailsId;
    private Long documentDetailsId;
    private Long rentDetailsId;
    private Long stayDetailsId;
    private Long paymentDetailsId;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Integer noOfDays;
    private String guestDetailsStatus;
    private boolean deleted;
    private String guestCurrentStatus;

    public static GuestInfoDto fromEntity(GuestDetails entity) {
        if (entity == null) return null;

        GuestInfoDto dto = new GuestInfoDto();
        dto.setRoomMasterId(entity.getRoomMasterId() == null ? null : entity.getRoomMasterId().longValue());
        dto.setPersonalDetailsId(entity.getPersonalDetailsId() == null ? null : entity.getPersonalDetailsId().longValue());
        dto.setDocumentDetailsId(entity.getDocumentDetailsId() == null ? null : entity.getDocumentDetailsId().longValue());
        dto.setRentDetailsId(entity.getRentDetailsId() == null ? null : entity.getRentDetailsId().longValue());
        dto.setStayDetailsId(entity.getStayDetailsId() == null ? null : entity.getStayDetailsId().longValue());
        dto.setCheckInDate(entity.getCheckInDate());
        dto.setCheckOutDate(entity.getCheckOutDate());
        dto.setCheckInTime(entity.getCheckInTime());
        dto.setCheckOutTime(entity.getCheckOutTime());
        dto.setNoOfDays(entity.getNoOfDays());
        dto.setGuestDetailsStatus(entity.getGuestDetailsStatus());
        return dto;
    }
}