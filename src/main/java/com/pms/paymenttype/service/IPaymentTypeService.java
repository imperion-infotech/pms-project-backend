/**
 * 
 */
package com.pms.paymenttype.service;

import java.util.List;
import java.util.Optional;

import com.pms.paymenttype.entity.PaymentType;

/**
 * 
 */
public interface IPaymentTypeService {
	
	List<PaymentType> getPaymentTypes();
	PaymentType createPaymentType(PaymentType PaymentType);
	PaymentType updatePaymentType(Long paymentTypeId, PaymentType paymentType);
	PaymentType getPaymentTypeById(Long id);
	public boolean deletePaymentType(Integer paymentTypeId);
	public List<PaymentType> search(String paymentTypeName,String paymentTypeShortName, String categoryName, String description);

}
