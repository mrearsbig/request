package com.mrearsbig.model.loantype;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoanTypeTest {

    @Test
    void testAllArgsConstructor() {
        LoanType loanType = new LoanType(1, "Personal", 1000.0, 5000.0, 5.5, true);
        assertEquals(1, loanType.getId());
        assertEquals("Personal", loanType.getName());
        assertEquals(1000.0, loanType.getMinAmount());
        assertEquals(5000.0, loanType.getMaxAmount());
        assertEquals(5.5, loanType.getInterestRate());
        assertTrue(loanType.getIsAutoVerified());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        LoanType loanType = new LoanType();
        loanType.setId(2);
        loanType.setName("Auto");
        loanType.setMinAmount(2000.0);
        loanType.setMaxAmount(10000.0);
        loanType.setInterestRate(3.2);
        loanType.setIsAutoVerified(false);

        assertEquals(2, loanType.getId());
        assertEquals("Auto", loanType.getName());
        assertEquals(2000.0, loanType.getMinAmount());
        assertEquals(10000.0, loanType.getMaxAmount());
        assertEquals(3.2, loanType.getInterestRate());
        assertFalse(loanType.getIsAutoVerified());
    }

    @Test
    void testBuilder() {
        LoanType loanType = LoanType.builder()
                .id(3)
                .name("Mortgage")
                .minAmount(50000.0)
                .maxAmount(500000.0)
                .interestRate(2.8)
                .isAutoVerified(true)
                .build();

        assertEquals(3, loanType.getId());
        assertEquals("Mortgage", loanType.getName());
        assertEquals(50000.0, loanType.getMinAmount());
        assertEquals(500000.0, loanType.getMaxAmount());
        assertEquals(2.8, loanType.getInterestRate());
        assertTrue(loanType.getIsAutoVerified());
    }

    @Test
    void testToBuilder() {
        LoanType original = LoanType.builder()
                .id(4)
                .name("Student")
                .minAmount(500.0)
                .maxAmount(20000.0)
                .interestRate(1.5)
                .isAutoVerified(false)
                .build();

        LoanType modified = original.toBuilder()
                .interestRate(1.2)
                .build();

        assertEquals(4, modified.getId());
        assertEquals("Student", modified.getName());
        assertEquals(500.0, modified.getMinAmount());
        assertEquals(20000.0, modified.getMaxAmount());
        assertEquals(1.2, modified.getInterestRate());
        assertFalse(modified.getIsAutoVerified());
    }
}