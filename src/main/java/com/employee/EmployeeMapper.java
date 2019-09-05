package com.employee;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeMapper {

    EmployeeDTO getEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeID(employee.getEmployeeID());
        employeeDTO.setFullName(employee.getFirstName() + ' ' + employee.getLastName());
        employeeDTO.setNotes(getNotesFromEmployee(employee));
        return employeeDTO;
    }

    private List<String> getNotesFromEmployee(Employee employee) {
        return employee
                .getNotes()
                .stream()
                .map(Note::getText)
                .collect(Collectors.toList());
    }

}
