package com.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController

public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //Create
    @PostMapping("/api/employees")
    public ResponseEntity<EmployeeModel> createEmployee(@RequestBody EmployeeModel employee) {
        EmployeeModel createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    //Read all Employees that exist
    @GetMapping("/api/employees")
    public ResponseEntity<List<EmployeeModel>> findEmployees() {
        List<EmployeeModel> employees = employeeService.findEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    //Read an EmployeeModel by employeeId
    @GetMapping("/api/employees/{employeeId}")
    public ResponseEntity<EmployeeModel> findEmployee(@PathVariable("employeeId") int employeeId) {
        EmployeeModel employee = employeeService.findEmployee(employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    //Update an EmployeeModel
    @PutMapping("/api/employees/{employeeId}")
    public ResponseEntity<EmployeeModel> updateEmployee(@PathVariable int employeeId, @RequestBody EmployeeModel employee) {
        EmployeeModel updatedEmployee = employeeService.updateEmployee(employeeId, employee);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    //Delete an EmployeeModel
    @DeleteMapping("/api/employees/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("employeeId") int employeeId) {
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
