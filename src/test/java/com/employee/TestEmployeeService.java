package com.employee;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class TestEmployeeService {

    private int employeeId;
    private Employee employee;
    private List<Employee> employees;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    private EmployeeService employeeService;

    @Before
    public void init() {

        employeeId = 1;
        employee = new Employee();
        employees = new ArrayList<>();
        employees.add(employee);

        employeeService = new EmployeeService(employeeRepository, employeeMapper);

    }

    @Test
    public void testCreateEmployee() {
        //given
        when(employeeRepository.save(employee)).thenReturn(employee);

        //when
        Employee createdEmployee = employeeService.createEmployee(employee);

        //then
        Assert.assertEquals("There was an error creating an employee", employee, createdEmployee);
    }

//    @Test
//    public void testFindEmployees() {
//        //given
//        when(employeeRepository.findAll()).thenReturn(employees);
//
//        //when
//        List<Employee> returnedEmployees = employeeService.findEmployees();
//
//        //then
//        Assert.assertEquals("There was an error getting the employees", employees, returnedEmployees);
//    }

    @Test
    public void testFindEmployee() {
        //given
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

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

    @Test
    public void testUpdateEmployee() {
        //given
        when(employeeRepository.save(employee)).thenReturn(employee);

        //when
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employee);

        //then
        Assert.assertEquals("There was an error updating the employee", employee, updatedEmployee);
    }

    @Test
    public void testUpdateEmployee_employeeIdUpdated() {
        //given
        when(employeeRepository.save(employee)).thenReturn(employee);

        //when
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employee);
        int updatedEmployeeId = updatedEmployee.getEmployeeID();

        //then
        Assert.assertEquals("There was an error updating the employee ID", employeeId, updatedEmployeeId);
    }

    @Test
    public void testDeleteEmployee_employeeExists_employeeIsDeleted() {
        //given
        when(employeeRepository.existsById(employeeId)).thenReturn(true);

        //when
        employeeService.deleteEmployee(employeeId);

        //then
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    public void testDeleteEmployee_employeeDoesNotExist_employeeIsNotDeleted() {
        //given
        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        //when
        employeeService.deleteEmployee(employeeId);

        //then
        verify(employeeRepository, times(0)).deleteById(anyInt());
    }

}
