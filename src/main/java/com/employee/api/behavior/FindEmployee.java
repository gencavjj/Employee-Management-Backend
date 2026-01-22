package com.employee.api.behavior;

import com.employee.api.model.EmployeeDTO;

public interface FindEmployee {
    EmployeeDTO findEmployee(int employeeId);
}
