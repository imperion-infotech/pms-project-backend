/**
 * 
 */
package com.pms.nightaudit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.nightaudit.entity.BusinessDate;
import com.pms.nightaudit.entity.BusinessDateRequest;
import com.pms.nightaudit.service.IBusinessDateService;
import com.pms.nightaudit.service.INightAuditService;

@RestController
@RequestMapping("/api/night-audit")
public class NightAuditController {

	@Autowired
    private INightAuditService service;
	
	private IBusinessDateService dateService;

    @PostMapping("/run-night-audit")
    public ResponseEntity<String> runNightAudit() {

        service.runNightAudit();

        return ResponseEntity.ok(
                "Night Audit Completed Successfully");
    }
    
    @PostMapping("/add-businessdate")
    public ResponseEntity<?> addBusinessDate(
            @RequestBody BusinessDateRequest request) {

        BusinessDate businessDate =
        		dateService.addBusinessDate(request);

        return ResponseEntity.ok(businessDate);
    }
}