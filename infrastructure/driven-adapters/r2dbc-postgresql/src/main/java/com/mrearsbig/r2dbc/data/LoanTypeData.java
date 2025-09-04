package com.mrearsbig.r2dbc.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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

@Table("loan_type")
public class LoanTypeData {
    @Id
    private Integer id;
    
    private String name;
    private Double minAmount;
    private Double maxAmount;
    private Double interestRate;

    @Column("automatic_validation")
    private Boolean isAutoVerified;
}
