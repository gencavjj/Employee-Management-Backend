package com.employee.internal;

import com.employee.api.model.EmployeeDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmployeeService.
 *
 * These tests verify the business logic in isolation.
 * Repository and Mapper are mocked - we're testing that the service:
 * - Correctly orchestrates calls to dependencies
 * - Handles edge cases appropriately
 * - Throws correct exceptions
 */
@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    private EmployeeService employeeService;

    private Employee sampleEmployee;
    private EmployeeDTO sampleEmployeeDTO;

    @Before
    public void setUp() {
        employeeService = new EmployeeService(employeeRepository, employeeMapper);

        sampleEmployee = createSampleEmployee(1, "John", "Doe", "john.doe@example.com");
        sampleEmployeeDTO = createSampleEmployeeDTO(1, "John", "Doe", "john.doe@example.com");
    }

    // ========== CREATE EMPLOYEE TESTS ==========

    @Test
    public void createEmployee_shouldMapDTOToEntity() {
        // Arrange
        when(employeeMapper.toEntity(sampleEmployeeDTO)).thenReturn(sampleEmployee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);

        // Act
        employeeService.createEmployee(sampleEmployeeDTO);

        // Assert
        verify(employeeMapper, times(1)).toEntity(sampleEmployeeDTO);
    }

    @Test
    public void createEmployee_shouldSaveToRepository() {
        // Arrange
        when(employeeMapper.toEntity(sampleEmployeeDTO)).thenReturn(sampleEmployee);
        when(employeeRepository.save(sampleEmployee)).thenReturn(sampleEmployee);

        // Act
        employeeService.createEmployee(sampleEmployeeDTO);

        // Assert
        verify(employeeRepository, times(1)).save(sampleEmployee);
    }

    // ========== FIND ALL EMPLOYEES TESTS ==========

    @Test
    public void findEmployees_shouldReturnAllEmployeesAsDTOs() {
        // Arrange
        Employee employee1 = createSampleEmployee(1, "John", "Doe", "john@example.com");
        Employee employee2 = createSampleEmployee(2, "Jane", "Smith", "jane@example.com");
        EmployeeDTO dto1 = createSampleEmployeeDTO(1, "John", "Doe", "john@example.com");
        EmployeeDTO dto2 = createSampleEmployeeDTO(2, "Jane", "Smith", "jane@example.com");

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));
        when(employeeMapper.toDTO(employee1)).thenReturn(dto1);
        when(employeeMapper.toDTO(employee2)).thenReturn(dto2);

        // Act
        List<EmployeeDTO> result = employeeService.findEmployees();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    public void findEmployees_whenNoEmployees_shouldReturnEmptyList() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<EmployeeDTO> result = employeeService.findEmployees();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void findEmployees_shouldCallRepositoryFindAll() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        employeeService.findEmployees();

        // Assert
        verify(employeeRepository, times(1)).findAll();
    }

    // ========== FIND EMPLOYEE BY ID TESTS ==========

    @Test
    public void findEmployee_whenExists_shouldReturnEmployeeDTO() {
        // Arrange
        when(employeeRepository.findById(1)).thenReturn(Optional.of(sampleEmployee));
        when(employeeMapper.toDTO(sampleEmployee)).thenReturn(sampleEmployeeDTO);

        // Act
        EmployeeDTO result = employeeService.findEmployee(1);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test(expected = RuntimeException.class)
    public void findEmployee_whenNotExists_shouldThrowRuntimeException() {
        // Arrange
        when(employeeRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        employeeService.findEmployee(999);

        // Assert - exception expected
    }

    @Test
    public void findEmployee_whenNotExists_exceptionHasCorrectMessage() {
        // Arrange
        when(employeeRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        try {
            employeeService.findEmployee(999);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Assert
            assertEquals("Employee does not exist", e.getMessage());
        }
    }

    @Test
    public void findEmployee_shouldCallRepositoryWithCorrectId() {
        // Arrange
        int employeeId = 42;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(sampleEmployee));
        when(employeeMapper.toDTO(sampleEmployee)).thenReturn(sampleEmployeeDTO);

        // Act
        employeeService.findEmployee(employeeId);

        // Assert
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    // ========== UPDATE EMPLOYEE TESTS ==========

    @Test
    public void updateEmployee_shouldSetEmployeeIdOnDTO() {
        // Arrange
        EmployeeDTO inputDTO = createSampleEmployeeDTO(null, "Updated", "Name", "updated@example.com");
        when(employeeMapper.toEntity(any(EmployeeDTO.class))).thenReturn(sampleEmployee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);

        // Act
        employeeService.updateEmployee(5, inputDTO);

        // Assert
        assertEquals(Integer.valueOf(5), inputDTO.getEmployeeID());
    }

    @Test
    public void updateEmployee_shouldMapAndSave() {
        // Arrange
        when(employeeMapper.toEntity(sampleEmployeeDTO)).thenReturn(sampleEmployee);
        when(employeeRepository.save(sampleEmployee)).thenReturn(sampleEmployee);

        // Act
        employeeService.updateEmployee(1, sampleEmployeeDTO);

        // Assert
        verify(employeeMapper, times(1)).toEntity(sampleEmployeeDTO);
        verify(employeeRepository, times(1)).save(sampleEmployee);
    }

    // ========== DELETE EMPLOYEE TESTS ==========

    @Test
    public void deleteEmployee_whenExists_shouldDelete() {
        // Arrange
        when(employeeRepository.existsById(1)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1);

        // Act
        employeeService.deleteEmployee(1);

        // Assert
        verify(employeeRepository, times(1)).deleteById(1);
    }

    @Test
    public void deleteEmployee_whenNotExists_shouldNotAttemptDelete() {
        // Arrange
        when(employeeRepository.existsById(999)).thenReturn(false);

        // Act
        employeeService.deleteEmployee(999);

        // Assert
        verify(employeeRepository, never()).deleteById(anyInt());
    }

    @Test
    public void deleteEmployee_shouldCheckExistenceFirst() {
        // Arrange
        when(employeeRepository.existsById(1)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1);

        // Act
        employeeService.deleteEmployee(1);

        // Assert
        verify(employeeRepository, times(1)).existsById(1);
    }

    // ========== HELPER METHODS ==========

    private Employee createSampleEmployee(Integer id, String firstName, String lastName, String email) {
        Employee employee = new Employee();
        employee.setEmployeeId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailAddress(email);
        return employee;
    }

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
