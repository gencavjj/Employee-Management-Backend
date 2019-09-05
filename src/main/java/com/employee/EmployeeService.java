package com.employee;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class EmployeeService {

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

    Employee findEmployee(int employeeID) {
        Optional<Employee> employee = employeeRepository.findById(employeeID);
        return employee.orElseThrow(() -> new RuntimeException("Employee does not exist"));
    }

    Employee updateEmployee(int employeeID, Employee employee) {
        employee.setEmployeeID(employeeID);
        return employeeRepository.save(employee);
    }

    void deleteEmployee(int employeeID) {
        if (employeeRepository.existsById(employeeID)) {
            employeeRepository.deleteById(employeeID);
        }

    }

}
