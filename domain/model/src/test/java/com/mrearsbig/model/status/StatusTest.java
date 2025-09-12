package com.mrearsbig.model.status;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Status status = new Status();
        status.setId(1);
        status.setName("Active");
        status.setDescription("The status is active");

        assertEquals(1, status.getId());
        assertEquals("Active", status.getName());
        assertEquals("The status is active", status.getDescription());
    }

    @Test
    void testAllArgsConstructor() {
        Status status = new Status(2, "Inactive", "The status is inactive");

        assertEquals(2, status.getId());
        assertEquals("Inactive", status.getName());
        assertEquals("The status is inactive", status.getDescription());
    }

    @Test
    void testBuilder() {
        Status status = Status.builder()
                .id(3)
                .name("Pending")
                .description("The status is pending")
                .build();

        assertEquals(3, status.getId());
        assertEquals("Pending", status.getName());
        assertEquals("The status is pending", status.getDescription());
    }

    @Test
    void testToBuilder() {
        Status original = Status.builder()
                .id(4)
                .name("Closed")
                .description("The status is closed")
                .build();

        Status modified = original.toBuilder()
                .name("Reopened")
                .build();

        assertEquals(4, modified.getId());
        assertEquals("Reopened", modified.getName());
        assertEquals("The status is closed", modified.getDescription());
    }
}