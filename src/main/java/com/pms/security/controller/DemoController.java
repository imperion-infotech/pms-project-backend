/**
 * 
 */
package com.pms.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pms.exception.BusinessException;
import com.pms.exception.ResourceNotFoundException;

import jakarta.validation.constraints.Min;

/**
 * 
 */
@RestController
public class DemoController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the public page!";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard() {
        return "Hello User!";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "Hello Admin!";
    }
    
    @GetMapping("/{id}")
    public String getResource(@PathVariable @Min(1) int id) {
        if (id == 404) {
            throw new ResourceNotFoundException("Resource with ID " + id + " not found");
        }
        if (id == 422) {
            throw new BusinessException("Business rule violated for resource " + id);
        }
        return "Resource " + id;
    }
    
    @GetMapping("/notfound")
    public String triggerNotFound() {
        throw new ResourceNotFoundException("The requested resource does not exist.");
    }

    @GetMapping("/business-error")
    public String triggerBusinessError() {
        throw new BusinessException("Business rule violated: Cannot process request.");
    }

    @GetMapping("/server-error")
    public String triggerServerError() {
        throw new ResourceNotFoundException("Unexpected server failure.");
    }
    
//    @PostMapping("/login")
//    public String login(@RequestBody String body) {
//        return "{\"message\":\"Login successful\"}";
//    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Audit Log!";
    }
}
