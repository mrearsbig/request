package com.mrearsbig.model.config;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {
    private final String code;
    private final Map<String, String> errors;

    public ApplicationException(String code, String message) {
        super(message);
        this.code = code;
        this.errors = null;
    }
}
