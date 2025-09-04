package com.mrearsbig.model.loantype;
import lombok.Builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanType {
    private Integer id;
    private String name;
    private Double minAmount;
    private Double maxAmount;
    private Double interestRate;
    private Boolean isAutoVerified;
}
