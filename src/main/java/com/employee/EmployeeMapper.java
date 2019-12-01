package com.employee;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeMapper {

    // TODO: getEmployeeForEmployeeDTO()
    Employee getEmployeeForEmployeeDTO(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        /**
         TODO:

         employeeDTO.getNotes()
                    .forEach(noteMessage -> {
                      Note note = new Note();
                      note.setText(noteMessage);
                      employee.addNote(note);
                    });

         return employee;
         * */
        return new Employee();
    }

    EmployeeDTO getEmployeeDTOForEmployee(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeID(employee.getEmployeeId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
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
