package com.mrearsbig.api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {
    @NotBlank(message = "The document is required")
    private String document;

    @NotNull(message = "The amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "The amount must be greater than zero")
    @DecimalMax(value = "15000000.0", inclusive = true, message = "The amount must be less than or equal to 15,000,000")
    private Double amount;

    @NotNull(message = "The term is required")
    @Min(1)
    @Max(60)
    private Integer term;

    @NotBlank(message = "The email is required")
    @Email(message = "The email must be valid")
    private String email;

    @NotNull(message = "The loan type is required")
    @Positive
    private Integer loanType;
}
