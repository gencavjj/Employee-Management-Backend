package com.employee;


import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TestEmployeeController {

    private int employeeId;
    private EmployeeDTO employeeDTO;
    private List<EmployeeDTO> employeeDTOS;
    private Employee employee;
    private List<Employee> employees;

    @Mock
    private EmployeeService employeeService;

    private EmployeeController employeeController;

    @Before
    public void init() {
        employeeId = 1;
        employeeDTO = new EmployeeDTO();
        employeeDTOS = new ArrayList<>();
        employeeDTOS.add(employeeDTO);
        employee = new Employee();
        employees = new ArrayList<>();
        employees.add(employee);

        employeeController = new EmployeeController(employeeService);
    }

    //Testing the createEmployee method
    @Test
    public void testCreateEmployees() {
        //given
        ResponseEntity<Employee> predictedResponse = new ResponseEntity<>(employee, HttpStatus.CREATED);
        when(employeeService.createEmployee(employee)).thenReturn(employee);

        //when
        ResponseEntity<Employee> actualResponse = employeeController.createEmployee(employee);

        //then
        assertEquals("There was an error creating the employee", predictedResponse, actualResponse);
    }

    //Testing the findEmployees method (find all)
    @Test
    public void testFindAllEmployees() {
        //given
        ResponseEntity<List<EmployeeDTO>> predictedResponse = new ResponseEntity<>(employeeDTOS, HttpStatus.OK);
        when(employeeService.findEmployees()).thenReturn(employeeDTOS);
        
        //when
        ResponseEntity<List<EmployeeDTO>> actualResponse = employeeController.findEmployees();

        //then
        assertEquals("There was an error finding the employees", predictedResponse, actualResponse);
    }

    //Testing the findEmployee method (find by Id)
    @Test
    public void testFindEmployeeById() {
        //given
        ResponseEntity<Employee> predictedResponse = new ResponseEntity<>(employee, HttpStatus.OK);
        when((employeeService.findEmployee(employeeId))).thenReturn(employee);

        //when
        ResponseEntity<Employee> actualResponse= employeeController.findEmployee(employeeId);

        //then
        assertEquals("There was an error finding the employee", actualResponse, predictedResponse);
    }

    //Testing the updateEmployee method
    @Test
    public void testUpdateEmployee() {
        //given
        ResponseEntity<Employee> predictedResponse = new ResponseEntity<>(employee, HttpStatus.OK);
        when((employeeService.updateEmployee(employeeId,employee))).thenReturn(employee);

        //when
        ResponseEntity<Employee> actualResponse = employeeController.updateEmployee(employeeId,employee);

        //then
        assertEquals("There was an error updating the employee", actualResponse, predictedResponse);
    }

    //Testing the deleteEmployee method
    @Test
    public void testDeleteEmployee() {
        //given
        ResponseEntity<?> predictedResponse = new ResponseEntity<>(employeeId,HttpStatus.OK);

        //when
        ResponseEntity<?> actualResponse = employeeController.deleteEmployee(employeeId);

        //then
        verify(employeeService, times(1)).deleteEmployee(employeeId);
    }

    //Testing that the employee is deleted
    @Test
    public void testDeleteEmployee_employeeIsDeleted() {
        //when
        employeeController.deleteEmployee(employeeId);

        //then
        verify(employeeService, times(1)).deleteEmployee(employeeId);
    }

}
