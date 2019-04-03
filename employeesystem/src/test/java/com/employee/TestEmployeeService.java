package com.employee;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class TestEmployeeService {

    private int employeeId;
    private Employee employee;
    private List<Employee> employees;

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    @Before
    public void init() {

        employeeId = 1;
        employee = new Employee();
        employees = new ArrayList<>();
        employees.add(employee);

        employeeService = new EmployeeService(employeeRepository);

    }

    @Test
    public void testFindEmployees() {

        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> returnedEmployees = employeeService.findEmployees();

        Assert.assertEquals("There was an error getting the employees", employees, returnedEmployees);

    }

    @Test
    public void testFindEmployee() {

        //given
        when(employeeRepository.findById(employeeId)).thenReturn(java.util.Optional.ofNullable(employee));

        //when
        Employee returnedEmployee = employeeService.findEmployee(employeeId);

        //then
        Assert.assertEquals("There was an error finding the employee", employee, returnedEmployee);
    }

    @Test(expected = RuntimeException.class)
    public void testFindEmployee_employeeDoesNotExist_runtimeExceptionIsThrown() {

        when(employeeRepository.findById(employeeId)).thenReturn(null);

        employeeService.findEmployee(employeeId);

    }

}
