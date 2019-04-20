package com.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //Create
    @PostMapping("/api/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    //Read all Employees that exist
    @GetMapping("/api/employees")
    public ResponseEntity<List<Employee>> findEmployees() {
        List<Employee> employees = employeeService.findEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    //Read an Employee by employeeId
    @GetMapping("/api/employees/{employeeId}")
    public ResponseEntity<Employee> findEmployee(@PathVariable("employee-id") int employeeId) {
        Employee employee = employeeService.findEmployee(employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    //Update an Employee
    @PutMapping("/api/employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int employeeId, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employee);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    //Delete an Employee
    @DeleteMapping("/api/employees/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("employee-id") int employeeId) {
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity(HttpStatus.OK);

    }

}