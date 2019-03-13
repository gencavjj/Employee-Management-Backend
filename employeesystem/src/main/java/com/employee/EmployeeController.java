package com.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/api/employees")
    public ResponseEntity<List<Employee>> findEmployees() {
        List<Employee> employees = employeeService.findEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/api/employees/{employee-id}")
    public ResponseEntity<Employee> findEmployee(@PathVariable("employee-id") int employeeId) {
        Employee employee = employeeService.findEmployee(employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

}
