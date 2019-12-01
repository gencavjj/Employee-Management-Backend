package com.employee;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class TestEmployeeService {

    private int employeeId;
    private EmployeeDTO employeeDTO;
    private List<EmployeeDTO> employeeDTOS;
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
        when(employeeMapper.getEmployeeForEmployeeDTO(employeeDTO)).thenReturn(employee);

        //when
        employeeService.createEmployee(employeeDTO);

        //then
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void testFindEmployees() {
        //given
        List<EmployeeDTO> predictedEmployeeDTOs = Collections.singletonList(employeeDTO);

        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee));
        when(employeeMapper.getEmployeeDTOForEmployee(employee)).thenReturn(employeeDTO);

        //when
        List<EmployeeDTO> returnedEmployeeDTOs = employeeService.findEmployees();

        //then
        Assert.assertEquals("There was an error getting the employees", predictedEmployeeDTOs, returnedEmployeeDTOs);

    }

    @Test
    public void testFindEmployee() {
        //given
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeMapper.getEmployeeDTOForEmployee(employee)).thenReturn(employeeDTO);

        //when
        EmployeeDTO returnedEmployee = employeeService.findEmployee(employeeId);

        //then
        Assert.assertEquals("There was an error finding the employee", employeeDTO, returnedEmployee);
    }

    @Test(expected = RuntimeException.class)
    public void testFindEmployee_employeeDoesNotExist_runtimeExceptionIsThrown() {

        when(employeeRepository.findById(employeeId)).thenReturn(null);

        employeeService.findEmployee(employeeId);
    }

    @Test
    public void testUpdateEmployee() {
        //given
        when(employeeMapper.getEmployeeForEmployeeDTO(employeeDTO)).thenReturn(employee);

        //when
        employeeService.updateEmployee(employeeId, employeeDTO);

        //then
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void testUpdateEmployee_employeeIdUpdated() {
        //given
        when(employeeMapper.getEmployeeForEmployeeDTO(employeeDTO)).thenReturn(employee);

        //when
<<<<<<< HEAD
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employee);
        int updatedEmployeeId = updatedEmployee.getEmployeeID();
=======
        employeeService.updateEmployee(employeeId, employeeDTO);
>>>>>>> ead1743c1f2c9e4722978fe422001499df0d2774

        //then
        verify(employeeRepository, times(1)).save(employee);
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
