package com.employee.api.behavior;

import com.employee.api.model.EmployeeDTO;

import java.util.List;

public interface FindEmployees {
    List<EmployeeDTO> findEmployees();
}
