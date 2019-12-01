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

    void createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.getEmployeeForEmployeeDTO(employeeDTO);
        employeeRepository.save(employee);

    }

    List<EmployeeDTO> findEmployees() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employeeMapper::getEmployeeDTOForEmployee)
                .collect(Collectors.toList());
    }

    EmployeeDTO findEmployee(int employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return employee.map(employeeMapper::getEmployeeDTOForEmployee).orElseThrow(() -> new RuntimeException("Employee does not exist"));
    }

    void updateEmployee(int employeeId, EmployeeDTO employeeDTO) {
        employeeDTO.setEmployeeId(employeeId);
        Employee employee = employeeMapper.getEmployeeForEmployeeDTO(employeeDTO);
        employeeRepository.save(employee);

    }

    void deleteEmployee(int employeeId) {
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
        }

    }

}
