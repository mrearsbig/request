package com.mrearsbig.model.application;

import com.mrearsbig.model.loantype.LoanType;
import com.mrearsbig.model.status.Status;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void builderAndGettersShouldWork() {
        Status approvedStatus = new Status(3, "Approved", "Application has been approved");
        LoanType personalLoanType = new LoanType(1, "Personal Loan", 0.05, 50000.0, 5.0, true);

        UUID id = UUID.randomUUID();
        String document = "123456789";
        Double amount = 10000.0;
        Integer term = 12;
        String email = "test@example.com";
        Status status = approvedStatus;
        LoanType loanType = personalLoanType;
        String name = "John Doe";
        Double baseSalary = 3000.0;
        Double monthlyPayment = 850.0;

        Application app = Application.builder()
                .id(id)
                .document(document)
                .amount(amount)
                .term(term)
                .email(email)
                .status(status)
                .loanType(loanType)
                .name(name)
                .baseSalary(baseSalary)
                .monthlyPayment(monthlyPayment)
                .build();

        assertEquals(id, app.getId());
        assertEquals(document, app.getDocument());
        assertEquals(amount, app.getAmount());
        assertEquals(term, app.getTerm());
        assertEquals(email, app.getEmail());
        assertEquals(status, app.getStatus());
        assertEquals(loanType, app.getLoanType());
        assertEquals(name, app.getName());
        assertEquals(baseSalary, app.getBaseSalary());
        assertEquals(monthlyPayment, app.getMonthlyPayment());
    }

    @Test
    void settersShouldWork() {
        Application app = new Application();
        UUID id = UUID.randomUUID();
        app.setId(id);
        app.setDocument("987654321");
        app.setAmount(5000.0);
        app.setTerm(24);
        app.setEmail("user@example.com");
        Status status = new Status(4, "Rejected", "Application has been rejected");
        app.setStatus(status);
        LoanType loanType = new LoanType(2, "Mortgage", 0.04, 300000.0, 30.0, true);
        app.setLoanType(loanType);
        app.setName("Jane Smith");
        app.setBaseSalary(4000.0);
        app.setMonthlyPayment(1200.0);

        assertEquals(id, app.getId());
        assertEquals("987654321", app.getDocument());
        assertEquals(5000.0, app.getAmount());
        assertEquals(24, app.getTerm());
        assertEquals("user@example.com", app.getEmail());
        // Comparar campos de status
        assertNotNull(app.getStatus());
        assertEquals(4, app.getStatus().getId());
        assertEquals("Rejected", app.getStatus().getName());
        assertEquals("Application has been rejected", app.getStatus().getDescription());
        // Comparar campos de loanType
        assertNotNull(app.getLoanType());
        assertEquals(2, app.getLoanType().getId());
        assertEquals("Mortgage", app.getLoanType().getName());
        assertEquals(0.04, app.getLoanType().getMinAmount());
        assertEquals(300000.0, app.getLoanType().getMaxAmount());
        assertEquals(30.0, app.getLoanType().getInterestRate());
        assertTrue(app.getLoanType().getIsAutoVerified());
        assertEquals("Jane Smith", app.getName());
        assertEquals(4000.0, app.getBaseSalary());
        assertEquals(1200.0, app.getMonthlyPayment());
    }

    @Test
    void noArgsConstructorShouldSetFieldsToNull() {
        Application app = new Application();
        assertNull(app.getId());
        assertNull(app.getDocument());
        assertNull(app.getAmount());
        assertNull(app.getTerm());
        assertNull(app.getEmail());
        assertNull(app.getStatus());
        assertNull(app.getLoanType());
        assertNull(app.getName());
        assertNull(app.getBaseSalary());
        assertNull(app.getMonthlyPayment());
    }

    @Test
    void allArgsConstructorShouldSetAllFields() {
        UUID id = UUID.randomUUID();
        String document = "111222333";
        Double amount = 2000.0;
        Integer term = 6;
        String email = "allargs@example.com";
        Status status = new Status(2, "Pending", "Application is pending review");
        LoanType loanType = new LoanType(1, "Auto", 0.03, 20000.0, 5.0, true);
        String name = "Alice";
        Double baseSalary = 2500.0;
        Double monthlyPayment = 400.0;

        Application app = new Application(id, document, amount, term, email, status, loanType, name, baseSalary, monthlyPayment);

        assertEquals(id, app.getId());
        assertEquals(document, app.getDocument());
        assertEquals(amount, app.getAmount());
        assertEquals(term, app.getTerm());
        assertEquals(email, app.getEmail());
        assertEquals(status, app.getStatus());
        assertEquals(loanType, app.getLoanType());
        assertEquals(name, app.getName());
        assertEquals(baseSalary, app.getBaseSalary());
        assertEquals(monthlyPayment, app.getMonthlyPayment());
    }
}