/**
 * 
 */
package com.pms.report.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.report.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

	@Autowired
	private ReportService reportService;

	@GetMapping("/buildings-report")
	public ResponseEntity<byte[]> downloadBuildingReport() {
		try {
			byte[] pdf = reportService.generateBuildingReport();

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=building_report.pdf")
					.contentType(MediaType.APPLICATION_PDF).body(pdf);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/booking-report")
	public ResponseEntity<byte[]> getBookingReport(@RequestParam Long hotelId,
			@RequestParam(defaultValue = "pdf") String format) {

		try {
			byte[] report = reportService.generateBookingReport(hotelId, format);

			// Set response headers based on format
			HttpHeaders headers = new HttpHeaders();

			if ("xlsx".equalsIgnoreCase(format)) {
				headers.setContentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
				headers.setContentDispositionFormData("attachment", "booking_report.xlsx");
			} else {
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDispositionFormData("attachment", "booking_report.pdf");
			}

			return ResponseEntity.ok().headers(headers).body(report);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/roomstatus-report")
	public ResponseEntity<byte[]> getRoomStatusReport(@RequestParam Long hotelId,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam LocalDate fromDate,
			@RequestParam LocalDate toDate, @RequestParam(defaultValue = "") Long buildingId,
			@RequestParam(defaultValue = "") Long floorId) {

		try {
			byte[] report = reportService.generateRoomStatusReport(hotelId, format, fromDate, toDate, buildingId,
					floorId);

			// Set response headers based on format
			HttpHeaders headers = new HttpHeaders();

			if ("xlsx".equalsIgnoreCase(format)) {
				headers.setContentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
				headers.setContentDispositionFormData("attachment", "room_status_report.xlsx");
			} else {
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDispositionFormData("attachment", "room_status_report.pdf");
			}

			return ResponseEntity.ok().headers(headers).body(report);

		} catch (Exception e) {
			System.out.println("Exception in room status report ::" + e);
			e.printStackTrace();

			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/dailycollection-report")
	public ResponseEntity<byte[]> getDailyCollectionReport(@RequestParam Long hotelId,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam LocalDate fromDate,
			@RequestParam LocalDate toDate) {

		try {
			byte[] report = reportService.generateDailyCollectionReport(hotelId, format, fromDate, toDate);

			// Set response headers based on format
			HttpHeaders headers = new HttpHeaders();

			if ("xlsx".equalsIgnoreCase(format)) {
				headers.setContentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
				headers.setContentDispositionFormData("attachment", "daily_collection_report.xlsx");
			} else {
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDispositionFormData("attachment", "daily_collection_report.pdf");
			}

			return ResponseEntity.ok().headers(headers).body(report);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/guest-list-report")
	public ResponseEntity<byte[]> getGuestListReport(@RequestParam Long hotelId,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam LocalDate fromDate,
			@RequestParam LocalDate toDate, @RequestParam(defaultValue = "") String guestStatus) {

		try {
			byte[] report = reportService.generateGuestListReport(hotelId, format, fromDate, toDate, guestStatus);

			// Set response headers based on format
			HttpHeaders headers = new HttpHeaders();

			if ("xlsx".equalsIgnoreCase(format)) {
				headers.setContentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
				headers.setContentDispositionFormData("attachment", "guest_list_report.xlsx");
			} else {
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDispositionFormData("attachment", "guest_list_report.pdf");
			}

			return ResponseEntity.ok().headers(headers).body(report);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/housekeeping-report")
	public ResponseEntity<byte[]> getHouseKeepingReport(@RequestParam Long hotelId,
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(defaultValue = "") Long buildingId,
			@RequestParam(defaultValue = "") Long floorId)
			{

		try {
			byte[] report = reportService.generateHouseKeepingReport(hotelId, format, buildingId,floorId);

			// Set response headers based on format
			HttpHeaders headers = new HttpHeaders();

			if ("xlsx".equalsIgnoreCase(format)) {
				headers.setContentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
				headers.setContentDispositionFormData("attachment", "house_keeping_report.xlsx");
			} else {
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDispositionFormData("attachment", "house_keeping_report.pdf");
			}

			return ResponseEntity.ok().headers(headers).body(report);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/shift-report")
	public ResponseEntity<byte[]> getShiftReport(@RequestParam Long hotelId,
	        @RequestParam(defaultValue = "pdf") String format,
	        @RequestParam LocalDate fromDate,
	        @RequestParam LocalDate toDate,
	        @RequestParam(required = false) Long userId) {

	    try {
	        byte[] report = reportService.generateShiftReport(hotelId, format, fromDate, toDate, userId);

	        HttpHeaders headers = new HttpHeaders();

	        if ("xlsx".equalsIgnoreCase(format)) {
	            headers.setContentType(
	                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
	            headers.setContentDispositionFormData("attachment", "shift_report.xlsx");
	        } else {
	            headers.setContentType(MediaType.APPLICATION_PDF);
	            headers.setContentDispositionFormData("attachment", "shift_report.pdf");
	        }

	        return ResponseEntity.ok().headers(headers).body(report);

	    } catch (Exception e) {
	        return ResponseEntity.internalServerError().build();
	    }
	}
	@GetMapping("/monthly-collection-payment-type-report")
	public ResponseEntity<byte[]> getMonthlyCollectionPaymentTypeReport(@RequestParam Long hotelId,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam LocalDate fromDate,
			@RequestParam LocalDate toDate) {

		try {
			byte[] report = reportService.generateMonthlyCollectionPaymentTypeReport(hotelId, format, fromDate, toDate);

			// Set response headers based on format
			HttpHeaders headers = new HttpHeaders();

			if ("xlsx".equalsIgnoreCase(format)) {
				headers.setContentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
				headers.setContentDispositionFormData("attachment", "monthly_collection_payment_type_report.xlsx");
			} else {
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDispositionFormData("attachment", "monthly_collection_payment_type_report.pdf");
			}

			return ResponseEntity.ok().headers(headers).body(report);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}