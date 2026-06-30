/**
 * 
 */
package com.pms.personaldetails.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.auditlog.context.BusinessTraceContext;
import com.pms.auditlog.util.AuditUtil;
import com.pms.personaldetails.PersonalDetails;
import com.pms.personaldetails.service.IPersonalDetailsService;
import com.pms.util.SnowflakeUtil;
import com.pms.util.TraceIdGenerator;

import jakarta.servlet.http.HttpSession;

/**
 * 
 */

import jakarta.validation.Valid;

@RestController
public class PersonalDetailsController {

	
    private final IPersonalDetailsService service;

    public PersonalDetailsController(IPersonalDetailsService service) {
        this.service = service;
    }
    
    @Autowired
	private SnowflakeUtil snowflakeUtil;
	

    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'PERSONALDETAILS_VIEW')")
    @GetMapping("/user/getfoliono")
	public ResponseEntity<Long> getFolioNo(){
		return new ResponseEntity<Long>(snowflakeUtil.generateId(), HttpStatus.OK);
	}

    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'PERSONALDETAILS_VIEW')")
//	@PreAuthorize("hasAuthority('PERSONALDETAILS_VIEW')")
    @GetMapping("/user/getpersonaldetails")
    public List<PersonalDetails> getAll() {
        return service.getAll();
    }

    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'PERSONALDETAILS_VIEW')")
//	@PreAuthorize("hasAuthority('PERSONALDETAILS_VIEW')")
    @GetMapping("/user/getpersonaldetail/{id}")
    public ResponseEntity<PersonalDetails> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'PERSONALDETAILS_CREATE')")
//	@PreAuthorize("hasAuthority('PERSONALDETAILS_CREATE')")
    @PostMapping(value= "/user/createpersonaldetail")
    public ResponseEntity<PersonalDetails> create(@Valid @RequestBody PersonalDetails details) throws IOException {
        
        String businessTraceId = TraceIdGenerator.generateBusinessTraceId();

        BusinessTraceContext.set(businessTraceId);

        PersonalDetails saved = service.create(details);

        return ResponseEntity.ok()
                .header("X-Business-Trace-Id", businessTraceId)
                .body(saved);
    }
    
    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'PERSONALDETAILS_UPDATE')")
//	@PreAuthorize("hasAuthority('PERSONALDETAILS_UPDATE')")
    @PutMapping("/user/updatepersonaldetail/{id}")
    public ResponseEntity<PersonalDetails> update(@PathVariable Long id, @Valid @RequestBody PersonalDetails details,HttpSession session) {
    	PersonalDetails existingPersonalDetails = service.getById(id);
    	if(existingPersonalDetails != null) {
    		session.setAttribute("oldValue", AuditUtil.toJson(service.getById(id)));
    	}
    	return ResponseEntity.ok(service.update(id, details));
    }

//	@PreAuthorize("hasAuthority('PERSONALDETAILS_DELETE')")
    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'PERSONALDETAILS_DELETE')")
    @DeleteMapping("/user/deletepersonaldetail/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Boolean flag = service.deletePersonalDetails(id);
        if(flag == true)
        return ResponseEntity.ok("Deleted successfully");
        else
        	return ResponseEntity.ok("Not Deleted successfully");
    }
    
    
    /*public ResponseEntity<String> delete(@PathVariable Long id) {
    	boolean isDeleted=  service.delete(id);
        if (isDeleted) {
			String responseContent = "personaldetail has been deleted successfully";
			return new ResponseEntity<String>(responseContent, HttpStatus.OK);
		}
		String error = "Error while deleting personaldetail from database";
		return new ResponseEntity<String>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
    
    
    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'PERSONALDETAILS_SEARCH')")
//	@PreAuthorize("hasAuthority('PERSONALDETAILS_SEARCH')")
    @GetMapping("/user/personaldetail/search")
    public List<PersonalDetails> searchPersonalDetails(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String nationality) {

        return service.search(firstName,lastName, email, phone, address,nationality);
    }
    
}

