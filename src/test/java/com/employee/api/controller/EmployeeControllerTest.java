package com.employee.api.controller;

import com.employee.api.behavior.*;
import com.employee.api.model.EmployeeDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmployeeController.
 *
 * These tests verify the controller's HTTP handling logic in isolation.
 * All behavior interfaces are mocked - we're only testing that the controller:
 * - Calls the correct behavior
 * - Returns the correct HTTP status
 * - Returns the correct response body
 */
@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {

    @Mock
    private CreateEmployee createEmployee;

    @Mock
    private FindEmployee findEmployee;

    @Mock
    private FindEmployees findEmployees;

    @Mock
    private UpdateEmployee updateEmployee;

    @Mock
    private DeleteEmployee deleteEmployee;

    private EmployeeController controller;

    private EmployeeDTO sampleEmployeeDTO;

    @Before
    public void setUp() {
        controller = new EmployeeController(
                createEmployee,
                findEmployee,
                findEmployees,
                updateEmployee,
                deleteEmployee
        );

        sampleEmployeeDTO = createSampleEmployeeDTO(1, "John", "Doe", "john.doe@example.com");
    }

    // ========== CREATE EMPLOYEE TESTS ==========

    @Test
    public void createEmployee_shouldReturnCreatedStatus() {
        // Arrange
        doNothing().when(createEmployee).createEmployee(any(EmployeeDTO.class));

        // Act
        ResponseEntity<?> response = controller.createEmployee(sampleEmployeeDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void createEmployee_shouldCallBehaviorWithCorrectDTO() {
        // Arrange
        doNothing().when(createEmployee).createEmployee(any(EmployeeDTO.class));

        // Act
        controller.createEmployee(sampleEmployeeDTO);

        // Assert
        verify(createEmployee, times(1)).createEmployee(sampleEmployeeDTO);
    }

    // ========== FIND ALL EMPLOYEES TESTS ==========

    @Test
    public void findEmployees_shouldReturnOkStatus() {
        // Arrange
        when(findEmployees.findEmployees()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<EmployeeDTO>> response = controller.findEmployees();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void findEmployees_shouldReturnListOfEmployees() {
        // Arrange
        List<EmployeeDTO> expectedEmployees = Arrays.asList(
                createSampleEmployeeDTO(1, "John", "Doe", "john@example.com"),
                createSampleEmployeeDTO(2, "Jane", "Smith", "jane@example.com")
        );
        when(findEmployees.findEmployees()).thenReturn(expectedEmployees);

        // Act
        ResponseEntity<List<EmployeeDTO>> response = controller.findEmployees();

        // Assert
        assertEquals(2, response.getBody().size());
        assertEquals(expectedEmployees, response.getBody());
    }

    @Test
    public void findEmployees_whenNoEmployeesExist_shouldReturnEmptyList() {
        // Arrange
        when(findEmployees.findEmployees()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<EmployeeDTO>> response = controller.findEmployees();

        // Assert
        assertTrue(response.getBody().isEmpty());
    }

    // ========== FIND EMPLOYEE BY ID TESTS ==========

    @Test
    public void findEmployee_shouldReturnOkStatus() {
        // Arrange
        when(findEmployee.findEmployee(1)).thenReturn(sampleEmployeeDTO);

        // Act
        ResponseEntity<EmployeeDTO> response = controller.findEmployee(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void findEmployee_shouldReturnCorrectEmployee() {
        // Arrange
        when(findEmployee.findEmployee(1)).thenReturn(sampleEmployeeDTO);

        // Act
        ResponseEntity<EmployeeDTO> response = controller.findEmployee(1);

        // Assert
        assertEquals(sampleEmployeeDTO, response.getBody());
        assertEquals("John", response.getBody().getFirstName());
    }

    @Test
    public void findEmployee_shouldCallBehaviorWithCorrectId() {
        // Arrange
        int employeeId = 42;
        when(findEmployee.findEmployee(employeeId)).thenReturn(sampleEmployeeDTO);

        // Act
        controller.findEmployee(employeeId);

        // Assert
        verify(findEmployee, times(1)).findEmployee(employeeId);
    }

    // ========== UPDATE EMPLOYEE TESTS ==========

    @Test
    public void updateEmployee_shouldReturnOkStatus() {
        // Arrange
        doNothing().when(updateEmployee).updateEmployee(eq(1), any(EmployeeDTO.class));

        // Act
        ResponseEntity<EmployeeDTO> response = controller.updateEmployee(1, sampleEmployeeDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updateEmployee_shouldCallBehaviorWithCorrectParameters() {
        // Arrange
        int employeeId = 1;
        doNothing().when(updateEmployee).updateEmployee(eq(employeeId), any(EmployeeDTO.class));

        // Act
        controller.updateEmployee(employeeId, sampleEmployeeDTO);

        // Assert
        verify(updateEmployee, times(1)).updateEmployee(employeeId, sampleEmployeeDTO);
    }

    // ========== DELETE EMPLOYEE TESTS ==========

    @Test
    public void deleteEmployee_shouldReturnOkStatus() {
        // Arrange
        doNothing().when(deleteEmployee).deleteEmployee(1);

        // Act
        ResponseEntity<?> response = controller.deleteEmployee(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteEmployee_shouldCallBehaviorWithCorrectId() {
        // Arrange
        int employeeId = 99;
        doNothing().when(deleteEmployee).deleteEmployee(employeeId);

        // Act
        controller.deleteEmployee(employeeId);

        // Assert
        verify(deleteEmployee, times(1)).deleteEmployee(employeeId);
    }

    // ========== HELPER METHODS ==========

    private EmployeeDTO createSampleEmployeeDTO(Integer id, String firstName, String lastName, String email) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeID(id);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setEmailAddress(email);
        dto.setNotes(Collections.emptyList());
        return dto;
    }
}
