package com.employee;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    Employee createEmployee(Employee employee) { return employeeRepository.save(employee); }

    List<Employee> findEmployees() {
        return employeeRepository.findAll();
    }

    Employee findEmployee(int employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee does not exist"));
    }

}
