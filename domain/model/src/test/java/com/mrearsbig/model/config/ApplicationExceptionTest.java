package com.mrearsbig.model.config;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationExceptionTest {

    @Test
    void constructorWithCodeAndErrors_setsFieldsCorrectly() {
        String code = "ERR001";
        Map<String, String> errors = new HashMap<>();
        errors.put("field", "must not be null");
        ApplicationException ex = new ApplicationException(code, errors);

        assertEquals(code, ex.getCode());
        assertEquals(errors, ex.getErrors());
        assertNull(ex.getMessage());
    }

    @Test
    void constructorWithCodeAndMessage_setsFieldsCorrectly() {
        String code = "ERR002";
        String message = "Something went wrong";
        ApplicationException ex = new ApplicationException(code, message);

        assertEquals(code, ex.getCode());
        assertEquals(message, ex.getMessage());
        assertNull(ex.getErrors());
    }

    @Test
    void isInstanceOfRuntimeException() {
        ApplicationException ex = new ApplicationException("ERR003", "error");
        assertTrue(ex instanceof RuntimeException);
    }
}