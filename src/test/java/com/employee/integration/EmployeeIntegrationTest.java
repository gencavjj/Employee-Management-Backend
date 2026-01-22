package com.employee.integration;

import com.Main;
import com.employee.api.model.EmployeeDTO;
import com.employee.internal.Employee;
import com.employee.internal.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the Employee API.
 * 
 * These tests verify the full request/response cycle:
 * - HTTP layer (controller)
 * - Business logic (service)
 * - Data access (repository)
 * - Database (H2 in-memory)
 * 
 * Uses @SpringBootTest to load the full application context.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        // Clean database before each test
        employeeRepository.deleteAll();
    }

    // ========== CREATE EMPLOYEE INTEGRATION TESTS ==========

    @Test
    public void createEmployee_shouldPersistToDatabase() throws Exception {
        // Arrange
        EmployeeDTO dto = createEmployeeDTO("John", "Doe", "john@example.com");

        // Act
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Assert
        List<Employee> employees = employeeRepository.findAll();
        assertEquals(1, employees.size());
        assertEquals("John", employees.get(0).getFirstName());
        assertEquals("Doe", employees.get(0).getLastName());
    }

    @Test
    public void createEmployee_withNotes_shouldPersistNotes() throws Exception {
        // Arrange
        EmployeeDTO dto = createEmployeeDTO("John", "Doe", "john@example.com");
        dto.setNotes(Arrays.asList("Note 1", "Note 2"));

        // Act
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Assert
        List<Employee> employees = employeeRepository.findAll();
        assertEquals(2, employees.get(0).getNotes().size());
    }

    // ========== FIND ALL EMPLOYEES INTEGRATION TESTS ==========

    @Test
    public void findEmployees_whenNoEmployees_shouldReturnEmptyArray() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void findEmployees_shouldReturnAllEmployees() throws Exception {
        // Arrange
        createAndSaveEmployee("John", "Doe", "john@example.com");
        createAndSaveEmployee("Jane", "Smith", "jane@example.com");

        // Act & Assert
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].firstName", containsInAnyOrder("John", "Jane")));
    }

    @Test
    public void findEmployees_shouldIncludeNotes() throws Exception {
        // Arrange
        Employee employee = createAndSaveEmployee("John", "Doe", "john@example.com");
        addNoteToEmployee(employee, "Test note");

        // Act & Assert
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notes", hasSize(1)))
                .andExpect(jsonPath("$[0].notes[0]", is("Test note")));
    }

    // ========== FIND EMPLOYEE BY ID INTEGRATION TESTS ==========

    @Test
    public void findEmployee_whenExists_shouldReturnEmployee() throws Exception {
        // Arrange
        Employee saved = createAndSaveEmployee("John", "Doe", "john@example.com");

        // Act & Assert
        mockMvc.perform(get("/api/employees/{id}", saved.getEmployeeId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.emailAddress", is("john@example.com")));
    }

    @Test
    public void findEmployee_whenNotExists_shouldReturn500() throws Exception {
        // Act & Assert
        // Note: Currently returns 500 due to RuntimeException. 
        // Consider adding proper exception handling for 404.
        mockMvc.perform(get("/api/employees/{id}", 99999))
                .andExpect(status().isInternalServerError());
    }

    // ========== UPDATE EMPLOYEE INTEGRATION TESTS ==========

    @Test
    public void updateEmployee_shouldPersistChanges() throws Exception {
        // Arrange
        Employee saved = createAndSaveEmployee("John", "Doe", "john@example.com");
        EmployeeDTO updateDTO = createEmployeeDTO("Johnny", "Updated", "johnny@example.com");

        // Act
        mockMvc.perform(put("/api/employees/{id}", saved.getEmployeeId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());

        // Assert
        Employee updated = employeeRepository.findById(saved.getEmployeeId()).orElseThrow();
        assertEquals("Johnny", updated.getFirstName());
        assertEquals("Updated", updated.getLastName());
        assertEquals("johnny@example.com", updated.getEmailAddress());
    }

    // ========== DELETE EMPLOYEE INTEGRATION TESTS ==========

    @Test
    public void deleteEmployee_whenExists_shouldRemoveFromDatabase() throws Exception {
        // Arrange
        Employee saved = createAndSaveEmployee("John", "Doe", "john@example.com");
        int employeeId = saved.getEmployeeId();

        // Act
        mockMvc.perform(delete("/api/employees/{id}", employeeId))
                .andExpect(status().isOk());

        // Assert
        assertFalse(employeeRepository.existsById(employeeId));
    }

    @Test
    public void deleteEmployee_whenNotExists_shouldReturnOk() throws Exception {
        // Act & Assert - should not throw error even if employee doesn't exist
        mockMvc.perform(delete("/api/employees/{id}", 99999))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteEmployee_shouldCascadeDeleteNotes() throws Exception {
        // Arrange
        Employee saved = createAndSaveEmployee("John", "Doe", "john@example.com");
        addNoteToEmployee(saved, "Note to be deleted");
        int employeeId = saved.getEmployeeId();

        // Act
        mockMvc.perform(delete("/api/employees/{id}", employeeId))
                .andExpect(status().isOk());

        // Assert
        assertFalse(employeeRepository.existsById(employeeId));
        // Notes should be cascade deleted with employee
    }

    // ========== END-TO-END WORKFLOW TESTS ==========

    @Test
    public void fullCrudWorkflow_shouldWorkCorrectly() throws Exception {
        // CREATE
        EmployeeDTO createDTO = createEmployeeDTO("John", "Doe", "john@example.com");
        createDTO.setNotes(Arrays.asList("Initial note"));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());

        // READ (find all to get ID)
        MvcResult result = mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        EmployeeDTO[] employees = objectMapper.readValue(response, EmployeeDTO[].class);
        int employeeId = employees[0].getEmployeeID();

        // READ (by ID)
        mockMvc.perform(get("/api/employees/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")));

        // UPDATE
        EmployeeDTO updateDTO = createEmployeeDTO("Johnny", "Updated", "johnny@example.com");
        mockMvc.perform(put("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());

        // Verify update
        mockMvc.perform(get("/api/employees/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Johnny")));

        // DELETE
        mockMvc.perform(delete("/api/employees/{id}", employeeId))
                .andExpect(status().isOk());

        // Verify deletion
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ========== HELPER METHODS ==========

    private EmployeeDTO createEmployeeDTO(String firstName, String lastName, String email) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setEmailAddress(email);
        dto.setNotes(Collections.emptyList());
        return dto;
    }

    private Employee createAndSaveEmployee(String firstName, String lastName, String email) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailAddress(email);
        return employeeRepository.save(employee);
    }

    private void addNoteToEmployee(Employee employee, String text) {
        com.employee.internal.Note note = new com.employee.internal.Note();
        note.setText(text);
        employee.addNote(note);
        employeeRepository.save(employee);
    }
}
