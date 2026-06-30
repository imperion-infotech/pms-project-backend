/**
 * 
 */
package com.pms.common.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.pms.baseentity.BaseEntity;
import com.pms.booking.Booking;
import com.pms.common.repository.SoftDeleteRepository;
import com.pms.exception.ResourceNotFoundException;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.repository.BookingRepository;

import jakarta.transaction.Transactional;

/**
 * 
 */
@Service
public class SoftDeleteService {

    @Transactional
    public <T extends BaseEntity, ID> T softDelete(ID id, SoftDeleteRepository<T, ID> repository) {

        Long hotelId = HotelContext.getHotelId();
        Long userId = UserContext.getUserId();

        T entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));

        if (!entity.getHotelId().equals(hotelId)) {
            throw new ResourceNotFoundException("Unauthorized access");
        }

        entity.setIsDeleted(true);
        entity.setIsActive(false);
        entity.setDeletedBy(userId);
        entity.setDeletedOn(LocalDateTime.now());
        entity.setUpdatedBy(userId);
        entity.setUpdatedOn(LocalDateTime.now());

        return repository.save(entity);
    }
	
}
