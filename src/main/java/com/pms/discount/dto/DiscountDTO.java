/**
 * 
 */
package com.pms.discount.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DiscountDTO {

    private Long id;
    
    private Long guestDetailsId;

    private String discountName;

    private BigDecimal percentage;

    private BigDecimal fixedDiscount;

    private LocalDateTime discountDatetime;

    private String remarks;

    private Boolean isActive;
    
    private Boolean isDeleted;
    
    private Long hotelId;
    
    
}