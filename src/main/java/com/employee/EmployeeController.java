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
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //Read all Employees that exist
    @GetMapping("/api/employees")
    public ResponseEntity<List<EmployeeDTO>> findEmployees() {
        List<EmployeeDTO> employees = employeeService.findEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    //Read an Employee by employeeId
    @GetMapping("/api/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> findEmployee(@PathVariable("employeeId") int employeeId) {
        EmployeeDTO employeeDTO = employeeService.findEmployee(employeeId);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    //Update an Employee
    @PutMapping("/api/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable int employeeId, @RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(employeeId, employeeDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Delete an Employee
    @DeleteMapping("/api/employees/{employeeID}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("employeeID") int employeeID) {
        employeeService.deleteEmployee(employeeID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
