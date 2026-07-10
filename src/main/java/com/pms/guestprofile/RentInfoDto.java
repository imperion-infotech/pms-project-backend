/**
 * 
 */
package com.pms.guestprofile;

import com.pms.rent.RentDetails;

import lombok.Data;

/**
 * 
 */
@Data
public class RentInfoDto {
    private Double rent;
    private Double basic;
    private Long taxId;
    private Double totalRental;
    private Double otherCharges;
    private Double discount;
    private Double totalCharges;
    private Double payments;
    private Double ccAuthorized;
    private Double deposite;
    private Double balance;
    private Long carryForwardAmount;

    public static RentInfoDto fromEntity(RentDetails entity) {
        if (entity == null) return null;

        RentInfoDto dto = new RentInfoDto();
        dto.setRent(entity.getRent());
        dto.setBasic(entity.getBasic());
        dto.setTaxId(entity.getTaxId());
        dto.setTotalRental(entity.getTotalRental());
        dto.setOtherCharges(entity.getOtherCharges());
        dto.setDiscount(entity.getDiscount());
        dto.setTotalCharges(entity.getTotalCharges());
        dto.setPayments(entity.getPayments());
        dto.setCcAuthorized(entity.getCcAuthorized());
        dto.setDeposite(entity.getDeposite());
        dto.setBalance(entity.getBalance());
        dto.setCarryForwardAmount(entity.getCarryForwardAmount());
        return dto;
    }
}