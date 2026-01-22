package com.employee.api.behavior;

import com.employee.api.model.EmployeeDTO;

public interface UpdateEmployee {
    void updateEmployee(int employeeId, EmployeeDTO employeeDTO);
}
