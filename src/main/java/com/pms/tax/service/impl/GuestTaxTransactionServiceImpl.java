/**
 * 
 */
package com.pms.tax.service.impl;

/**
 * 
 */
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.booking.Booking;
import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.service.IGuestDetailsService;
import com.pms.rent.RentDetails;
import com.pms.rent.services.IRentDetailsService;
import com.pms.security.configuration.HotelContext;
import com.pms.security.repository.BookingRepository;
import com.pms.tax.dto.GuestTaxTransactionResponse;
import com.pms.tax.entity.GuestTaxTransaction;
import com.pms.tax.repository.GuestTaxTransactionRepository;
import com.pms.tax.service.IGuestTaxTransactionService;
import com.pms.taxmaster.dao.TaxMastersRepository;
import com.pms.taxmaster.entity.TaxMaster;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestTaxTransactionServiceImpl implements IGuestTaxTransactionService {

	private final GuestTaxTransactionRepository taxTxnRepo;
	private final TaxMastersRepository taxMasterRepo;
	private final BookingRepository bookingRepo;

	private final IGuestDetailsService guestDetailsService;

	private final IRentDetailsService rentDetailsService;

	@Override
	public void saveGuestTaxes(Long bookingId) {

		Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));

		GuestDetails guestDetails = guestDetailsService.getGuestDetail(booking.getGuestDetailsId());

		RentDetails rentDetails = rentDetailsService
				.getRentDetailByPersonalDetailsId(Long.valueOf(guestDetails.getPersonalDetailsId()));

		Double roomRent = rentDetails.getRent() == null ? 0 : rentDetails.getRent();

		Long hotelId = HotelContext.getHotelId();

		List<TaxMaster> taxes = taxMasterRepo.findByHotelIdAndIsActiveTrueAndIsDeletedFalse(hotelId);

		List<GuestTaxTransaction> transactions = new ArrayList<>();

		for (TaxMaster tax : taxes) {

			BigDecimal taxAmount;

			if (Boolean.TRUE.equals(tax.getPerDayTax())) {

				long noOfDays = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());

				taxAmount = BigDecimal.valueOf(tax.getAmount()).multiply(BigDecimal.valueOf(noOfDays));
			} else {

				taxAmount = BigDecimal.valueOf(roomRent).multiply(BigDecimal.valueOf(tax.getAmount()))
						.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
			}

			GuestTaxTransaction txn = new GuestTaxTransaction();

			txn.setBooking(booking);
			txn.setTaxMaster(tax);
			txn.setTaxName(tax.getTaxMasterName());
			txn.setTaxRate(tax.getAmount());
			txn.setTaxableAmount(BigDecimal.valueOf(roomRent));
			txn.setTaxAmount(taxAmount);

			transactions.add(txn);
		}

		taxTxnRepo.saveAll(transactions);
	}

	@Override
	@Transactional(readOnly = true)
	public List<GuestTaxTransactionResponse> getTaxesByBooking(Long bookingId) {

		List<GuestTaxTransaction> transactions = taxTxnRepo.findByBookingIdAndIsDeletedFalse(bookingId);

		return transactions.stream().map(this::convertToResponse).toList();
	}

	@Transactional
	@Override
	public void updateGuestTaxes(Long bookingId) {

		Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));

		GuestDetails guestDetails = guestDetailsService.getGuestDetail(booking.getGuestDetailsId());

		RentDetails rentDetails = rentDetailsService
				.getRentDetailByPersonalDetailsId(Long.valueOf(guestDetails.getPersonalDetailsId()));

		Double roomRent = rentDetails.getRent() == null ? 0 : rentDetails.getRent();

		long noOfDays = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());

		List<GuestTaxTransaction> transactions = taxTxnRepo.findByBookingId(bookingId);

		for (GuestTaxTransaction txn : transactions) {

			TaxMaster tax = txn.getTaxMaster();

			BigDecimal taxAmount;

			if (Boolean.TRUE.equals(tax.getPerDayTax())) {

				taxAmount = BigDecimal.valueOf(tax.getAmount()).multiply(BigDecimal.valueOf(noOfDays));

			} else {

				taxAmount = BigDecimal.valueOf(roomRent).multiply(BigDecimal.valueOf(tax.getAmount()))
						.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
			}

			txn.setTaxRate(tax.getAmount());
			txn.setTaxableAmount(BigDecimal.valueOf(roomRent));
			txn.setTaxAmount(taxAmount);
		}

		taxTxnRepo.saveAll(transactions);
	}

	private GuestTaxTransactionResponse convertToResponse(GuestTaxTransaction txn) {

		GuestTaxTransactionResponse response = new GuestTaxTransactionResponse();

		response.setId(txn.getId());

		response.setBookingId(txn.getBooking() != null ? txn.getBooking().getId() : null);

		response.setTaxMasterId(txn.getTaxMaster() != null ? txn.getTaxMaster().getId() : null);

		response.setTaxName(txn.getTaxName());

		response.setTaxRate(txn.getTaxRate());

		response.setTaxableAmount(txn.getTaxableAmount());

		response.setTaxAmount(txn.getTaxAmount());

		return response;
	}
}