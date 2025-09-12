package com.mrearsbig.usecase.application;

import com.mrearsbig.model.application.Application;
import com.mrearsbig.model.application.gateways.ApplicationRepository;
import com.mrearsbig.model.config.ApplicationException;
import com.mrearsbig.model.loantype.LoanType;
import com.mrearsbig.model.loantype.gateways.LoanTypeRepository;
import com.mrearsbig.model.status.Status;
import com.mrearsbig.model.status.gateways.StatusRepository;
import com.mrearsbig.model.user.gateways.AuthenticationGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

class ApplicationUseCaseTest {
    @Test
    void getApplicationsForManualReview_shouldReturnEnrichedApplications() {
        Application app = Application.builder()
                .email("test@email.com")
                .document("123456")
                .amount(10000.0)
                .term(12)
                .loanType(LoanType.builder().id(1).build())
                .status(Status.builder().id(2).build())
                .build();

        LoanType loanType = LoanType.builder().id(1).interestRate(0.05).build();
        Status status = Status.builder().id(2).name("Pending").build();
        com.mrearsbig.model.user.User user = com.mrearsbig.model.user.User.builder()
                .firstName("John").lastName("Doe").baseSalary(2000.0).build();

        when(applicationRepository.findAllPendingForReview(0, 1)).thenReturn(reactor.core.publisher.Flux.just(app));
        when(loanTypeRepository.findById(1)).thenReturn(Mono.just(loanType));
        when(statusRepository.findById(2)).thenReturn(Mono.just(status));
        when(authenticationGateway.findByEmail("test@email.com", token)).thenReturn(Mono.just(user));

        StepVerifier.create(applicationUseCase.getApplicationsForManualReview(0, 1, token))
                .assertNext(result -> {
                    assert result.getName().equals("John Doe");
                    assert result.getBaseSalary().equals(2000.0);
                    assert result.getLoanType().getInterestRate().equals(0.05);
                    assert result.getMonthlyPayment() != null;
                })
                .verifyComplete();
    }

    @Test
    void getApplicationsForManualReview_shouldHandleLoanTypeNotFound() {
        Application app = Application.builder()
                .email("test@email.com")
                .document("123456")
                .amount(10000.0)
                .term(12)
                .loanType(LoanType.builder().id(1).build())
                .status(Status.builder().id(2).build())
                .build();

        when(applicationRepository.findAllPendingForReview(0, 1)).thenReturn(reactor.core.publisher.Flux.just(app));
        when(loanTypeRepository.findById(1)).thenReturn(Mono.empty());
        when(statusRepository.findById(2)).thenReturn(Mono.just(Status.builder().id(2).build()));
        when(authenticationGateway.findByEmail("test@email.com", token)).thenReturn(Mono.just(com.mrearsbig.model.user.User.builder().build()));

        StepVerifier.create(applicationUseCase.getApplicationsForManualReview(0, 1, token))
                .verifyComplete();
    }

    @Test
    void getApplicationsForManualReview_shouldHandleStatusNotFound() {
        Application app = Application.builder()
                .email("test@email.com")
                .document("123456")
                .amount(10000.0)
                .term(12)
                .loanType(LoanType.builder().id(1).build())
                .status(Status.builder().id(2).build())
                .build();

        when(applicationRepository.findAllPendingForReview(0, 1)).thenReturn(reactor.core.publisher.Flux.just(app));
        when(loanTypeRepository.findById(1)).thenReturn(Mono.just(LoanType.builder().id(1).build()));
        when(statusRepository.findById(2)).thenReturn(Mono.empty());
        when(authenticationGateway.findByEmail("test@email.com", token)).thenReturn(Mono.just(com.mrearsbig.model.user.User.builder().build()));

        StepVerifier.create(applicationUseCase.getApplicationsForManualReview(0, 1, token))
                .verifyComplete();
    }

    @Test
    void getApplicationsForManualReview_shouldHandleUserNotFound() {
        Application app = Application.builder()
                .email("test@email.com")
                .document("123456")
                .amount(10000.0)
                .term(12)
                .loanType(LoanType.builder().id(1).build())
                .status(Status.builder().id(2).build())
                .build();

        when(applicationRepository.findAllPendingForReview(0, 1)).thenReturn(reactor.core.publisher.Flux.just(app));
        when(loanTypeRepository.findById(1)).thenReturn(Mono.just(LoanType.builder().id(1).interestRate(0.05).build()));
        when(statusRepository.findById(2)).thenReturn(Mono.just(Status.builder().id(2).build()));
        when(authenticationGateway.findByEmail("test@email.com", token)).thenReturn(Mono.empty());

        StepVerifier.create(applicationUseCase.getApplicationsForManualReview(0, 1, token))
                .verifyComplete();
    }

    private ApplicationRepository applicationRepository;
    private LoanTypeRepository loanTypeRepository;
    private StatusRepository statusRepository;
    private AuthenticationGateway authenticationGateway;
    private ApplicationUseCase applicationUseCase;

    private final String token = "test-token";

    @BeforeEach
    void setUp() {
        applicationRepository = mock(ApplicationRepository.class);
        loanTypeRepository = mock(LoanTypeRepository.class);
        statusRepository = mock(StatusRepository.class);
        authenticationGateway = mock(AuthenticationGateway.class);
        applicationUseCase = new ApplicationUseCase(
                applicationRepository,
                loanTypeRepository,
                statusRepository,
                authenticationGateway
        );
    }

    @Test
    void execute_shouldReturnSavedApplication_whenAllValid() {
        Application inputApp = Application.builder()
                .email("test@email.com")
                .document("123456")
                .loanType(LoanType.builder().id(1).build())
                .build();

        LoanType loanType = LoanType.builder().id(1).interestRate(0.05).build();
        Status status = Status.builder().id(2).build();
        Application savedApp = inputApp.toBuilder().loanType(loanType).status(status).build();

        when(authenticationGateway.existsByEmailAndDocument("test@email.com", "123456", token))
                .thenReturn(Mono.just(true));
        when(loanTypeRepository.findById(1)).thenReturn(Mono.just(loanType));
        when(statusRepository.findById(2)).thenReturn(Mono.just(status));
        when(applicationRepository.save(any(Application.class))).thenReturn(Mono.just(savedApp));

        StepVerifier.create(applicationUseCase.execute(inputApp, token))
                .expectNext(savedApp)
                .verifyComplete();

        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    void execute_shouldReturnError_whenUserDoesNotExist() {
        Application inputApp = Application.builder()
                .email("notfound@email.com")
                .document("000000")
                .loanType(LoanType.builder().id(1).build())
                .build();

        when(authenticationGateway.existsByEmailAndDocument("notfound@email.com", "000000", token))
                .thenReturn(Mono.just(false));

        StepVerifier.create(applicationUseCase.execute(inputApp, token))
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof ApplicationException;
                    ApplicationException ex = (ApplicationException) throwable;
                    assert ex.getCode().equals("REQ_404");
                    assert ex.getMessage().contains("User does not exist");
                })
                .verify();

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void execute_shouldReturnError_whenLoanTypeNotFound() {
        Application inputApp = Application.builder()
                .email("test@email.com")
                .document("123456")
                .loanType(LoanType.builder().id(99).build())
                .build();

        when(authenticationGateway.existsByEmailAndDocument(anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(true));
        when(loanTypeRepository.findById(99)).thenReturn(Mono.empty());
        when(statusRepository.findById(anyInt())).thenReturn(Mono.empty());

        StepVerifier.create(applicationUseCase.execute(inputApp, token))
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof ApplicationException;
                    ApplicationException ex = (ApplicationException) throwable;
                    assert ex.getCode().equals("REQ_404");
                    assert ex.getMessage().contains("Loan type not found");
                })
                .verify();

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void execute_shouldReturnError_whenStatusNotFound() {
        Application inputApp = Application.builder()
                .email("test@email.com")
                .document("123456")
                .loanType(LoanType.builder().id(1).build())
                .build();

        LoanType loanType = LoanType.builder().id(1).build();

        when(authenticationGateway.existsByEmailAndDocument(anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(true));
        when(loanTypeRepository.findById(1)).thenReturn(Mono.just(loanType));
        when(statusRepository.findById(anyInt())).thenReturn(Mono.empty());

        StepVerifier.create(applicationUseCase.execute(inputApp, token))
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof ApplicationException;
                    ApplicationException ex = (ApplicationException) throwable;
                    assert ex.getCode().equals("REQ_404");
                    assert ex.getMessage().contains("Status not found");
                })
                .verify();

        verify(applicationRepository, never()).save(any());
    }
}