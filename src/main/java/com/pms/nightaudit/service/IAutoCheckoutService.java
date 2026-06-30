/**
 * 
 */
package com.pms.nightaudit.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dao.impl.GuestDetailsRepository;
import com.pms.security.configuration.HotelContext;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */

public interface IAutoCheckoutService
{

     public void processDueOutGuests(LocalDate businessDate);
}