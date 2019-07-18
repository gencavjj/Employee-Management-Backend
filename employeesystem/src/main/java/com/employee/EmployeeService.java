package com.employee;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;

    }

    EmployeeModel createEmployee(EmployeeModel employee) { return employeeRepository.save(employee); }

    List<EmployeeModel> findEmployees() {
        return employeeRepository.findAll();
    }

    EmployeeModel findEmployee(int employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("EmployeeModel does not exist"));
    }

    EmployeeModel updateEmployee(int employeeId, EmployeeModel employee) {
        employee.setEmployeeId(employeeId);
        return employeeRepository.save(employee);
    }

    void deleteEmployee(int employeeId) {
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
        }

    }

}
