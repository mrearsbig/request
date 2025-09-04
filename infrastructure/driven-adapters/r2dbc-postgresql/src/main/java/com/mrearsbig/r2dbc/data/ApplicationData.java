package com.mrearsbig.r2dbc.data;

import java.util.UUID;

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

@Table("application")
public class ApplicationData {
    @Id
    private UUID id;
    
    private String document;
    private Double amount;
    private Integer term;
    private String email;

    @Column("status_id")
    private Integer status;

    @Column("loan_type_id")
    private Integer loanType;
}
