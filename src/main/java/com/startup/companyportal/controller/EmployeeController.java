package com.startup.companyportal.controller;

import com.startup.companyportal.exception.AccessForbiddenException;
import com.startup.companyportal.exception.ResourceNotFoundException;
import com.startup.companyportal.model.Employee;
import com.startup.companyportal.model.EmployeeLogin;
import com.startup.companyportal.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/company-portal/v1/")
public class EmployeeController {

    @Autowired
    private EmployeeRepo employeeRepo;

    //Get Employee
    @GetMapping("/employees")
    public List<Employee> getAllEmpoyee() {
        return employeeRepo.findAll();
    }

    //Get Employee with ID
    @GetMapping("/employees/{email}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "email") String email)
            throws ResourceNotFoundException {
        Employee employee = employeeRepo.findById(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + email));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        System.out.println("Hello :"+ employee.getFirstName());
        return employeeRepo.save(employee);
    }

    @PutMapping("/employees/{email}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "email") String email,
                                                   @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Employee employee = employeeRepo.findById(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + email));

        employee.setEmail(employeeDetails.getEmail());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());
        final Employee updatedEmployee = employeeRepo.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{email}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "email") String email)
            throws ResourceNotFoundException {
        Employee employee = employeeRepo.findById(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + email));

        employeeRepo.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @GetMapping("/login")
    public ResponseEntity<Employee> loginEmployee(@RequestBody EmployeeLogin eLogin) throws ResourceNotFoundException,AccessForbiddenException{
        Employee employee = employeeRepo.findById(eLogin.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + eLogin.getEmail()));
        Map<String, Boolean> response = new HashMap<>();
        if(eLogin.getPassword().equals(employee.getPassword())) {
            return ResponseEntity.ok(employee);
        } else {
            throw  new AccessForbiddenException("Employee Password is incorrect");
        }
    }
}
