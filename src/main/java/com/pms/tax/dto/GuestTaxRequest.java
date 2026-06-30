/**
 * 
 */
package com.pms.tax.dto;

/**
 * 
 */
import java.math.BigDecimal;

import lombok.Data;

@Data
public class GuestTaxRequest {

    private Long bookingId;

    private BigDecimal roomRent;
}