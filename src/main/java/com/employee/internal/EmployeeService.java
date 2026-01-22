package com.employee.internal;

import com.employee.api.behavior.*;
import com.employee.api.model.EmployeeDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService implements
        CreateEmployee,
        FindEmployee,
        FindEmployees,
        UpdateEmployee,
        DeleteEmployee {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public void createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeDTO> findEmployees() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO findEmployee(int employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return employee.map(employeeMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Employee does not exist"));
    }

    @Override
    public void updateEmployee(int employeeId, EmployeeDTO employeeDTO) {
        employeeDTO.setEmployeeID(employeeId);
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(int employeeId) {
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
        }
    }

}
