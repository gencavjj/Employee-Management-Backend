package com.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
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
    public ResponseEntity<List<EmployeeDTO>> findEmployees() {
        List<EmployeeDTO> employees = employeeService.findEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    //Read an Employee by employeeId
    @GetMapping("/api/employees/{employeeID}")
    public ResponseEntity<Employee> findEmployee(@PathVariable("employeeID") int employeeID) {
        Employee employee = employeeService.findEmployee(employeeID);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    //Update an Employee
    @PutMapping("/api/employees/{employeeID}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int employeeID, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(employeeID, employee);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    //Delete an Employee
    @DeleteMapping("/api/employees/{employeeID}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("employeeID") int employeeID) {
        employeeService.deleteEmployee(employeeID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
