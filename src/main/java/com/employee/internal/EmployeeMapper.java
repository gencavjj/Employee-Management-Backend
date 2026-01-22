package com.employee.internal;

import com.employee.api.model.EmployeeDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeMapper {

    Employee toEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmployeeId(employeeDTO.getEmployeeID());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmailAddress(employeeDTO.getEmailAddress());
        if (employeeDTO.getNotes() != null) {
            employeeDTO.getNotes()
                    .forEach(noteMessage -> {
                        Note note = new Note();
                        note.setText(noteMessage);
                        employee.addNote(note);
                    });
        }

        return employee;
    }

    EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeID(employee.getEmployeeId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setEmailAddress(employee.getEmailAddress());
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
