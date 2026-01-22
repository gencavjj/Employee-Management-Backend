package com.employee.api.controller;

import com.employee.api.behavior.*;
import com.employee.api.model.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class EmployeeController {

    private final CreateEmployee createEmployee;
    private final FindEmployee findEmployee;
    private final FindEmployees findEmployees;
    private final UpdateEmployee updateEmployee;
    private final DeleteEmployee deleteEmployee;

    @Autowired
    public EmployeeController(
            CreateEmployee createEmployee,
            FindEmployee findEmployee,
            FindEmployees findEmployees,
            UpdateEmployee updateEmployee,
            DeleteEmployee deleteEmployee
    ) {
        this.createEmployee = createEmployee;
        this.findEmployee = findEmployee;
        this.findEmployees = findEmployees;
        this.updateEmployee = updateEmployee;
        this.deleteEmployee = deleteEmployee;
    }

    @PostMapping("/api/employees")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        createEmployee.createEmployee(employeeDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/api/employees")
    public ResponseEntity<List<EmployeeDTO>> findEmployees() {
        List<EmployeeDTO> employees = findEmployees.findEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/api/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> findEmployee(@PathVariable("employeeId") int employeeId) {
        EmployeeDTO employeeDTO = findEmployee.findEmployee(employeeId);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    @PutMapping("/api/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable int employeeId, @RequestBody EmployeeDTO employeeDTO) {
        updateEmployee.updateEmployee(employeeId, employeeDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/employees/{employeeID}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("employeeID") int employeeID) {
        deleteEmployee.deleteEmployee(employeeID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
