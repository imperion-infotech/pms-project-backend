/**
 * 
 */
package com.pms.bookinghistory.repository;

/**
 * 
 */
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.bookinghistory.entity.BookingHistory;

public interface BookingHistoryRepository
        extends JpaRepository<BookingHistory, Long> {

    List<BookingHistory> findByBookingIdOrderByCreatedOnDesc(Long bookingId);

    List<BookingHistory> findByGuestDetailsIdOrderByCreatedOnDesc(Long guestDetailsId);

    List<BookingHistory> findByHotelIdOrderByCreatedOnDesc(Long hotelId);
}