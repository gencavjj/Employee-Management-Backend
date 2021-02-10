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
    //private Employee employee;
    //private List<Employee> employees;

    @Mock
    private EmployeeService employeeService;

    private EmployeeController employeeController;

    @Before
    public void init() {
        employeeId = 1;
        employeeDTO = new EmployeeDTO();
        employeeDTOS = new ArrayList<>();
        employeeDTOS.add(employeeDTO);
        //employee = new Employee();
        //employees = new ArrayList<>();
        //employees.add(employee);

        employeeController = new EmployeeController(employeeService);
    }

    //Testing the createEmployee method
    @Test
    public void testCreateEmployees() {
        //given
        ResponseEntity<?> predictedResponse = new ResponseEntity<>(employeeDTO, HttpStatus.CREATED);

        //when
        ResponseEntity<?> actualResponse = employeeController.createEmployee(employeeDTO);

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
        ResponseEntity<EmployeeDTO> predictedResponse = new ResponseEntity<>(employeeDTO, HttpStatus.OK);
        when((employeeService.findEmployee(employeeId))).thenReturn(employeeDTO);

        //when
        ResponseEntity<EmployeeDTO> actualResponse= employeeController.findEmployee(employeeId);

        //then
        assertEquals("There was an error finding the employee", actualResponse, predictedResponse);
    }

    //Testing the updateEmployee method
    @Test
    public void testUpdateEmployee() {
        //given
        ResponseEntity<?> predictedResponse = new ResponseEntity<>(employeeDTO, HttpStatus.OK);

        //when
        ResponseEntity<?> actualResponse = employeeController.updateEmployee(employeeId,employeeDTO);

        //then
        assertEquals("There was an error updating the employee", actualResponse, predictedResponse);
    }

    /**

     First 'delete' test should assert that the appropriate response is received.

     Since we return an OK status whenever something is deleted, this will be our predicted value.

     Second 'delete' test should verify that the correct method was called with the desired arguments.

     * */

    //Testing the deleteEmployee method
    @Test
    public void testDeleteEmployee() {
        //given
        ResponseEntity<?> predictedResponse = new ResponseEntity<>(HttpStatus.OK);

        //when
        ResponseEntity<?> actualResponse = employeeController.deleteEmployee(employeeId);

        //then
        assertEquals("There was an error deleting the employee", actualResponse, predictedResponse);
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
