package com.startup.companyportal.controller;

import com.startup.companyportal.exception.ResourceNotFoundException;
import com.startup.companyportal.model.Employee;
import com.startup.companyportal.model.EmployeeLogin;
import com.startup.companyportal.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        System.out.println("Hello :"+ employee.getFirstName());
        return employeeRepo.save(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
                                                   @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employee.setEmail(employeeDetails.getEmail());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());
        final Employee updatedEmployee = employeeRepo.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employeeRepo.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @GetMapping("/login")
    public Map<String, Boolean> loginEmployee(@RequestBody EmployeeLogin eLogin) throws ResourceNotFoundException{
        Employee employee = employeeRepo.findById(eLogin.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + eLogin.getId()));
        Map<String, Boolean> response = new HashMap<>();
        if(eLogin.getPassword().equals(employee.getPassword())) {
            response.put("authorized",Boolean.TRUE);
        } else {
            response.put("authorized",Boolean.FALSE);
        }
        return response;
    }
}
