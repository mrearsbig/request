package com.mrearsbig.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ApplicationUpdate(@NotNull(message = "The status is required") @Positive(message = "The status must be a positive number") Integer status) {
}
