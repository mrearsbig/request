package com.mrearsbig.api.dto;

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
public class ApplicationReviewResponse {
    private String document;
    private Double amount;
    private Integer term;
    private String email;
    private String name; // 👈 depende si lo tienes en Application o lo consultas vía join
    private Integer status;
    private Integer loanType;
    private Double interestRate;
    private Double baseSalary;
    private Double monthlyPayment; // calculado
}
