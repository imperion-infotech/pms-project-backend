/**
 * 
 */
package com.pms.nightaudit.service;

import java.time.LocalDate;

/**
 * 
 */
public interface INoShowService {

    void processNoShows(LocalDate businessDate);
}