package com.employee;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeMapper {

    Employee getEmployeeForEmployeeDTO(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmployeeId(employeeDTO.getEmployeeId());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employeeDTO.getNotes()
                    .forEach(noteMessage -> {
                      Note note = new Note();
                      note.setText(noteMessage);
                      employee.addNote(note);
                    });

         return employee;
    }

    EmployeeDTO getEmployeeDTOForEmployee(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
<<<<<<< HEAD
        employeeDTO.setEmployeeID(employee.getEmployeeID());
        employeeDTO.setFullName(employee.getFirstName() + ' ' + employee.getLastName());
=======
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
>>>>>>> ead1743c1f2c9e4722978fe422001499df0d2774
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
