/**
 * 
 */
package com.pms.booking.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pms.booking.Employee;

/**
 * 
 */
@RestController
public class EmployeeController {

    @GetMapping(value = "/auth/getemployee/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Employee getEmployee(@PathVariable int id) {
        return new Employee(id, "John Doe");
    }

    @PostMapping(value = "/auth/createemployee", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Employee createEmployee(@RequestBody Employee employee) {
        // Simulate saving to DB
        employee.setName(employee.getName() + " (saved)");
        return employee;
    }
}
