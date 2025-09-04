package com.mrearsbig.api.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApplicationResponse {
    private UUID id;
    private String document;
    private Double amount;
    private Integer term;
    private String email;
    private Integer status;
    private Integer loanType;
}
