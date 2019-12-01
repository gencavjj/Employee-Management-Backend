package com.employee;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    List<EmployeeDTO> findEmployees() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employeeMapper::getEmployeeDTO)
                .collect(Collectors.toList());
    }

    Employee findEmployee(int employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return employee.orElseThrow(() -> new RuntimeException("Employee does not exist"));
    }

    Employee updateEmployee(int employeeId, Employee employee) {
        employee.setEmployeeId(employeeId);
        return employeeRepository.save(employee);
    }

    void deleteEmployee(int employeeId) {
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
        }

    }

}
