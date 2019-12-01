package com.employee;

import java.io.Serializable;
import java.util.List;

public class EmployeeDTO implements Serializable {

    private Integer employeeId;
    private String firstName;
    private String lastName;
    private List<String> notes;


    public Integer getEmployeeId() { return employeeId; }

    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getNotes() { return notes; }

    public void setNotes(List<String> notes) { this.notes = notes; }
}
