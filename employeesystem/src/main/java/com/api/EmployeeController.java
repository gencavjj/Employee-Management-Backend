package com.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/api/employees")
    public ResponseEntity<List<Employee>> findEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/api/employees/{employee-id}")
    public ResponseEntity<Employee> findEmployee(@PathVariable("employee-id") int employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return new ResponseEntity<>(employee.orElseThrow(() -> new RuntimeException("Employee does not exist.")), HttpStatus.OK);
    }

}
