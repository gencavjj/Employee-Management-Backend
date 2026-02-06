package com.employee.api;

import com.Main;
import com.employee.api.model.EmployeeDTO;
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

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API Contract Tests for Employee endpoints.
 *
 * These tests verify HTTP-specific behavior:
 * - Content types
 * - HTTP methods
 * - Response codes
 * - URL patterns
 *
 * Useful for ensuring API contract stability.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class EmployeeApiContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        employeeRepository.deleteAll();
    }

    // ========== CONTENT TYPE TESTS ==========

    @Test
    public void getEmployees_shouldReturnJsonContentType() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getEmployee_shouldReturnJsonContentType() throws Exception {
        // Arrange
        var employee = createAndSaveEmployee();

        // Act & Assert
        mockMvc.perform(get("/api/employees/{id}", employee.getEmployeeId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createEmployee_shouldAcceptJsonContentType() throws Exception {
        EmployeeDTO dto = createEmployeeDTO();

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    // ========== HTTP METHOD TESTS ==========

    @Test
    public void employeesEndpoint_shouldSupportGet() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk());
    }

    @Test
    public void employeesEndpoint_shouldSupportPost() throws Exception {
        EmployeeDTO dto = createEmployeeDTO();

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void employeeByIdEndpoint_shouldSupportGet() throws Exception {
        var employee = createAndSaveEmployee();

        mockMvc.perform(get("/api/employees/{id}", employee.getEmployeeId()))
                .andExpect(status().isOk());
    }

    @Test
    public void employeeByIdEndpoint_shouldSupportPut() throws Exception {
        var employee = createAndSaveEmployee();
        EmployeeDTO dto = createEmployeeDTO();

        mockMvc.perform(put("/api/employees/{id}", employee.getEmployeeId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void employeeByIdEndpoint_shouldSupportDelete() throws Exception {
        var employee = createAndSaveEmployee();

        mockMvc.perform(delete("/api/employees/{id}", employee.getEmployeeId()))
                .andExpect(status().isOk());
    }

    // ========== URL PATTERN TESTS ==========

    @Test
    public void apiUrlsShouldBePrefixedWithApi() throws Exception {
        // Verify /api/employees works
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk());

        // Verify /employees without /api prefix doesn't work
        mockMvc.perform(get("/employees"))
                .andExpect(status().isNotFound());
    }

    // ========== RESPONSE STRUCTURE TESTS ==========

    @Test
    public void getEmployees_shouldReturnArrayStructure() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getEmployee_shouldReturnObjectStructure() throws Exception {
        var employee = createAndSaveEmployee();

        mockMvc.perform(get("/api/employees/{id}", employee.getEmployeeId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.employeeID").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.emailAddress").exists())
                .andExpect(jsonPath("$.notes").exists());
    }

    @Test
    public void employeeResponse_notesShouldBeArrayOfStrings() throws Exception {
        var employee = createAndSaveEmployee();
        addNoteToEmployee(employee, "Test note");

        mockMvc.perform(get("/api/employees/{id}", employee.getEmployeeId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").isArray())
                .andExpect(jsonPath("$.notes[0]").isString());
    }

    // ========== CORS TESTS ==========

    @Test
    public void shouldAllowCorsFromConfiguredOrigin() throws Exception {
        mockMvc.perform(get("/api/employees")
                        .header("Origin", "http://localhost:4200"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }

    // ========== HELPER METHODS ==========

    private EmployeeDTO createEmployeeDTO() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName("Test");
        dto.setLastName("Employee");
        dto.setEmailAddress("test@example.com");
        dto.setNotes(Collections.emptyList());
        return dto;
    }

    private com.employee.internal.Employee createAndSaveEmployee() {
        com.employee.internal.Employee employee = new com.employee.internal.Employee();
        employee.setFirstName("Test");
        employee.setLastName("Employee");
        employee.setEmailAddress("test@example.com");
        return employeeRepository.save(employee);
    }

    private void addNoteToEmployee(com.employee.internal.Employee employee, String text) {
        com.employee.internal.Note note = new com.employee.internal.Note();
        note.setText(text);
        employee.addNote(note);
        employeeRepository.save(employee);
    }
}
