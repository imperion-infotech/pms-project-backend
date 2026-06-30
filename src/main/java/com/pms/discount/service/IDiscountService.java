/**
 * 
 */
package com.pms.discount.service;

import java.util.List;

import com.pms.discount.dto.DiscountDTO;

/**
 * 
 */
public interface IDiscountService {
	
	  DiscountDTO createDiscount(DiscountDTO dto);

	    DiscountDTO updateDiscount(Long id, DiscountDTO dto);

	    DiscountDTO getByDiscountId(Long id);

	    List<DiscountDTO> getAllDiscount();

	    Boolean deleteDiscount(Long id);
	    
	    List<DiscountDTO> getDiscountsByGuestId(Long guestId);

}
