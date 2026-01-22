package com.employee.internal;

import com.employee.api.model.EmployeeDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for EmployeeMapper.
 * 
 * These tests verify the mapping logic between entities and DTOs.
 * No mocking needed - this is pure transformation logic.
 */
public class EmployeeMapperTest {

    private EmployeeMapper mapper;

    @Before
    public void setUp() {
        mapper = new EmployeeMapper();
    }

    // ========== TO ENTITY TESTS ==========

    @Test
    public void toEntity_shouldMapBasicFields() {
        // Arrange
        EmployeeDTO dto = createEmployeeDTO(1, "John", "Doe", "john@example.com");

        // Act
        Employee entity = mapper.toEntity(dto);

        // Assert
        assertEquals(Integer.valueOf(1), entity.getEmployeeId());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("john@example.com", entity.getEmailAddress());
    }

    @Test
    public void toEntity_shouldMapNotes() {
        // Arrange
        EmployeeDTO dto = createEmployeeDTO(1, "John", "Doe", "john@example.com");
        dto.setNotes(Arrays.asList("Note 1", "Note 2", "Note 3"));

        // Act
        Employee entity = mapper.toEntity(dto);

        // Assert
        assertEquals(3, entity.getNotes().size());
        assertEquals("Note 1", entity.getNotes().get(0).getText());
        assertEquals("Note 2", entity.getNotes().get(1).getText());
        assertEquals("Note 3", entity.getNotes().get(2).getText());
    }

    @Test
    public void toEntity_notesHaveBackReferenceToEmployee() {
        // Arrange
        EmployeeDTO dto = createEmployeeDTO(1, "John", "Doe", "john@example.com");
        dto.setNotes(Arrays.asList("Test note"));

        // Act
        Employee entity = mapper.toEntity(dto);

        // Assert
        assertSame(entity, entity.getNotes().get(0).getEmployee());
    }

    @Test
    public void toEntity_withNullNotes_shouldNotThrowException() {
        // Arrange
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeID(1);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmailAddress("john@example.com");
        dto.setNotes(null);

        // Act
        Employee entity = mapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertTrue(entity.getNotes().isEmpty());
    }

    @Test
    public void toEntity_withEmptyNotes_shouldReturnEmptyNotesList() {
        // Arrange
        EmployeeDTO dto = createEmployeeDTO(1, "John", "Doe", "john@example.com");
        dto.setNotes(Collections.emptyList());

        // Act
        Employee entity = mapper.toEntity(dto);

        // Assert
        assertTrue(entity.getNotes().isEmpty());
    }

    @Test
    public void toEntity_withNullId_shouldMapOtherFields() {
        // Arrange
        EmployeeDTO dto = createEmployeeDTO(null, "John", "Doe", "john@example.com");

        // Act
        Employee entity = mapper.toEntity(dto);

        // Assert
        assertNull(entity.getEmployeeId());
        assertEquals("John", entity.getFirstName());
    }

    // ========== TO DTO TESTS ==========

    @Test
    public void toDTO_shouldMapBasicFields() {
        // Arrange
        Employee entity = createEmployee(1, "Jane", "Smith", "jane@example.com");

        // Act
        EmployeeDTO dto = mapper.toDTO(entity);

        // Assert
        assertEquals(Integer.valueOf(1), dto.getEmployeeID());
        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("jane@example.com", dto.getEmailAddress());
    }

    @Test
    public void toDTO_shouldMapNotesToStringList() {
        // Arrange
        Employee entity = createEmployee(1, "Jane", "Smith", "jane@example.com");
        addNoteToEmployee(entity, "First note");
        addNoteToEmployee(entity, "Second note");

        // Act
        EmployeeDTO dto = mapper.toDTO(entity);

        // Assert
        assertEquals(2, dto.getNotes().size());
        assertEquals("First note", dto.getNotes().get(0));
        assertEquals("Second note", dto.getNotes().get(1));
    }

    @Test
    public void toDTO_withNoNotes_shouldReturnEmptyNotesList() {
        // Arrange
        Employee entity = createEmployee(1, "Jane", "Smith", "jane@example.com");

        // Act
        EmployeeDTO dto = mapper.toDTO(entity);

        // Assert
        assertNotNull(dto.getNotes());
        assertTrue(dto.getNotes().isEmpty());
    }

    // ========== ROUND TRIP TESTS ==========

    @Test
    public void roundTrip_basicFieldsShouldBePreserved() {
        // Arrange
        EmployeeDTO originalDTO = createEmployeeDTO(1, "John", "Doe", "john@example.com");

        // Act
        Employee entity = mapper.toEntity(originalDTO);
        EmployeeDTO resultDTO = mapper.toDTO(entity);

        // Assert
        assertEquals(originalDTO.getEmployeeID(), resultDTO.getEmployeeID());
        assertEquals(originalDTO.getFirstName(), resultDTO.getFirstName());
        assertEquals(originalDTO.getLastName(), resultDTO.getLastName());
        assertEquals(originalDTO.getEmailAddress(), resultDTO.getEmailAddress());
    }

    @Test
    public void roundTrip_notesShouldBePreserved() {
        // Arrange
        EmployeeDTO originalDTO = createEmployeeDTO(1, "John", "Doe", "john@example.com");
        originalDTO.setNotes(Arrays.asList("Note A", "Note B"));

        // Act
        Employee entity = mapper.toEntity(originalDTO);
        EmployeeDTO resultDTO = mapper.toDTO(entity);

        // Assert
        assertEquals(originalDTO.getNotes(), resultDTO.getNotes());
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    public void toEntity_withSpecialCharactersInFields_shouldMapCorrectly() {
        // Arrange
        EmployeeDTO dto = createEmployeeDTO(1, "José", "O'Brien", "jose.obrien@example.com");
        dto.setNotes(Arrays.asList("Note with émojis 🎉", "Special chars: <>&\"'"));

        // Act
        Employee entity = mapper.toEntity(dto);

        // Assert
        assertEquals("José", entity.getFirstName());
        assertEquals("O'Brien", entity.getLastName());
        assertEquals("Note with émojis 🎉", entity.getNotes().get(0).getText());
    }

    @Test
    public void toDTO_withEmptyStrings_shouldMapCorrectly() {
        // Arrange
        Employee entity = createEmployee(1, "", "", "");

        // Act
        EmployeeDTO dto = mapper.toDTO(entity);

        // Assert
        assertEquals("", dto.getFirstName());
        assertEquals("", dto.getLastName());
        assertEquals("", dto.getEmailAddress());
    }

    // ========== HELPER METHODS ==========

    private EmployeeDTO createEmployeeDTO(Integer id, String firstName, String lastName, String email) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeID(id);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setEmailAddress(email);
        dto.setNotes(Collections.emptyList());
        return dto;
    }

    private Employee createEmployee(Integer id, String firstName, String lastName, String email) {
        Employee employee = new Employee();
        employee.setEmployeeId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailAddress(email);
        return employee;
    }

    private void addNoteToEmployee(Employee employee, String text) {
        Note note = new Note();
        note.setText(text);
        employee.addNote(note);
    }
}
