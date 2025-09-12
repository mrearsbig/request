package com.mrearsbig.model.application;
import lombok.Builder;

import java.util.UUID;

import com.mrearsbig.model.loantype.LoanType;
import com.mrearsbig.model.status.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Application {
    private UUID id;
    private String document;
    private Double amount;
    private Integer term;
    private String email;
    private Status status;
    private LoanType loanType;

    // Campos adicionales
    private String name;
    private Double baseSalary;
    private Double monthlyPayment;
}
