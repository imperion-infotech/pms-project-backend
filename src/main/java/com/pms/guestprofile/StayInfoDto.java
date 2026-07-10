/**
 * 
 */
package com.pms.guestprofile;

import com.pms.stay.entity.RateTypeEnum;
import com.pms.stay.entity.StayDetails;
import com.pms.stay.entity.StayStatusEnum;

import lombok.Data;

/**
 * 
 */
@Data
public class StayInfoDto {
    private Long floorId;
    private Long buildingId;
    private Long roomTypeId;
    private Long roomMasterId;
    private String comment;
    private RateTypeEnum rateTypeEnum;
    private StayStatusEnum stayStatusEnum;
    private Integer noOfGuest;
    private Boolean taxExempt;

    public static StayInfoDto fromEntity(StayDetails entity) {
        if (entity == null) return null;

        StayInfoDto dto = new StayInfoDto();
        dto.setFloorId(entity.getFloorId());
        dto.setBuildingId(entity.getBuildingId());
        dto.setRoomTypeId(entity.getRoomTypeId());
        dto.setRoomMasterId(entity.getRoomMasterId());
        dto.setComment(entity.getComment());
        dto.setRateTypeEnum(entity.getRateTypeEnum());
        dto.setStayStatusEnum(entity.getStayStatusEnum());
        dto.setNoOfGuest(entity.getNoOfGuest());
        dto.setTaxExempt(entity.getTaxExempt());
        return dto;
    }
}