package com.mrearsbig.model.user;

import java.time.LocalDate;
import java.util.UUID;

import javax.management.relation.Role;

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
public class User {
    private UUID id;
    private String firstName;
    private String lastName;
    private String document;
    private LocalDate birthdate;
    private String address;
    private String phoneNumber;
    private String email;
    private String password;
    private Double baseSalary;
    private Integer role;
}
