package com.mrearsbig.model.user;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testNoArgsConstructorAndSettersAndGetters() {
        User user = new User();
        UUID id = UUID.randomUUID();
        String firstName = "John";
        String lastName = "Doe";
        String document = "123456789";
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        String phoneNumber = "555-1234";
        String email = "john.doe@example.com";
        String password = "password";
        Double baseSalary = 50000.0;
        Integer role = 1;

        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDocument(document);
        user.setBirthdate(birthdate);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setPassword(password);
        user.setBaseSalary(baseSalary);
        user.setRole(role);

        assertEquals(id, user.getId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(document, user.getDocument());
        assertEquals(birthdate, user.getBirthdate());
        assertEquals(address, user.getAddress());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(baseSalary, user.getBaseSalary());
        assertEquals(role, user.getRole());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        String firstName = "Jane";
        String lastName = "Smith";
        String document = "987654321";
        LocalDate birthdate = LocalDate.of(1985, 5, 15);
        String address = "456 Elm St";
        String phoneNumber = "555-5678";
        String email = "jane.smith@example.com";
        String password = "secure";
        Double baseSalary = 60000.0;
        Integer role = 2;

        User user = new User(id, firstName, lastName, document, birthdate, address, phoneNumber, email, password, baseSalary, role);

        assertEquals(id, user.getId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(document, user.getDocument());
        assertEquals(birthdate, user.getBirthdate());
        assertEquals(address, user.getAddress());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(baseSalary, user.getBaseSalary());
        assertEquals(role, user.getRole());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .firstName("Alice")
                .lastName("Johnson")
                .document("111222333")
                .birthdate(LocalDate.of(2000, 12, 31))
                .address("789 Oak Ave")
                .phoneNumber("555-0000")
                .email("alice.johnson@example.com")
                .password("alicepass")
                .baseSalary(70000.0)
                .role(3)
                .build();

        assertEquals(id, user.getId());
        assertEquals("Alice", user.getFirstName());
        assertEquals("Johnson", user.getLastName());
        assertEquals("111222333", user.getDocument());
        assertEquals(LocalDate.of(2000, 12, 31), user.getBirthdate());
        assertEquals("789 Oak Ave", user.getAddress());
        assertEquals("555-0000", user.getPhoneNumber());
        assertEquals("alice.johnson@example.com", user.getEmail());
        assertEquals("alicepass", user.getPassword());
        assertEquals(70000.0, user.getBaseSalary());
        assertEquals(3, user.getRole());
    }

    @Test
    void testToBuilder() {
        User original = User.builder()
                .firstName("Bob")
                .lastName("Brown")
                .build();

        User copy = original.toBuilder().lastName("White").build();

        assertEquals("Bob", copy.getFirstName());
        assertEquals("White", copy.getLastName());
        assertEquals("Brown", original.getLastName());
    }
}