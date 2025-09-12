package com.mrearsbig.model.config;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void testAllArgsConstructor() {
        String code = "ERR001";
        Map<String, String> errors = new HashMap<>();
        errors.put("field1", "must not be null");
        errors.put("field2", "must be positive");

        ValidationException ex = new ValidationException(code, errors);

        assertEquals(code, ex.getCode());
        assertEquals(errors, ex.getErrors());
        assertNull(ex.getMessage());
    }

    @Test
    void testConstructorWithCodeAndMessage() {
        String code = "ERR002";
        String message = "Validation failed";

        ValidationException ex = new ValidationException(code, message);

        assertEquals(code, ex.getCode());
        assertNull(ex.getErrors());
        assertEquals(message, ex.getMessage());
    }

    @Test
    void testInheritance() {
        ValidationException ex = new ValidationException("ERR003", "Some error");
        assertTrue(ex instanceof RuntimeException);
    }
}