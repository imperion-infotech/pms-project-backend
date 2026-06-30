/**
 * 
 */
package com.pms.nightaudit.service.impl;


import org.springframework.stereotype.Service;

import com.pms.nightaudit.service.ITaxPostingService;

@Service
public class TaxPostingServiceImpl
        implements ITaxPostingService {

    @Override
    public Double postTaxes(
            Double roomRevenue) {

        // Example GST 18%

        return roomRevenue * 0.18;
    }
}