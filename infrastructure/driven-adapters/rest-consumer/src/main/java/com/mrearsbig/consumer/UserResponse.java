package com.mrearsbig.consumer;

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
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String document;
    private String birthdate; // en JSON es String "1995-10-20"
    private String address;
    private String phoneNumber;
    private String email;
    private String password;
    private Double baseSalary;
    private Integer role;
}
