package com.mrearsbig.usecase.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import com.mrearsbig.model.application.Application;
import com.mrearsbig.model.application.gateways.ApplicationRepository;
import com.mrearsbig.model.application.gateways.NotificationPublisher;
import com.mrearsbig.model.config.ApplicationException;
import com.mrearsbig.model.loantype.LoanType;
import com.mrearsbig.model.loantype.gateways.LoanTypeRepository;
import com.mrearsbig.model.status.Status;
import com.mrearsbig.model.status.gateways.StatusRepository;
import com.mrearsbig.model.user.User;
import com.mrearsbig.model.user.gateways.AuthenticationGateway;

import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Slf4j
@RequiredArgsConstructor
public class ApplicationUseCase {
    private final ApplicationRepository applicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StatusRepository statusRepository;
    private final AuthenticationGateway authenticationGateway;
    private final NotificationPublisher notificationPublisher;

    private static final Integer APPROVED_ID = 1;
    private static final Integer PENDING_REVIEW_ID = 2;
    private static final Integer REJECTED_ID = 3;

    public Mono<Application> execute(Application application, String token) {
        System.out.printf("Start execute application: %s%n", application);

        // 1. Validar que el usuario exista en el MS de auth
        return authenticationGateway.existsByEmailAndDocument(
                application.getEmail(),
                application.getDocument(),
                token) // <-- aquí ya pasas el token
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ApplicationException("REQ_404",
                                "User does not exist in authentication service"));
                    }
                    return Mono.just(application);
                })
                // 2. LoanType y Status
                .flatMap(app -> loanTypeRepository.findById(app.getLoanType().getId())
                        .switchIfEmpty(Mono.error(new ApplicationException("REQ_404", "Loan type not found")))
                        .zipWith(statusRepository.findById(PENDING_REVIEW_ID)
                                .switchIfEmpty(Mono.error(new ApplicationException("REQ_404", "Status not found"))))
                        .flatMap(tuple -> {
                            LoanType loanType = tuple.getT1();
                            Status status = tuple.getT2();

                            Application newApplication = application.toBuilder()
                                    .loanType(loanType)
                                    .status(status)
                                    .build();

                            return applicationRepository.save(newApplication);
                        }));
    }

    // Método privado para calcular la cuota mensual usando el sistema francés
    private Double calculateFrenchMonthlyPayment(Double amount, Double annualInterestRate, Integer termMonths) {
        if (amount == null || annualInterestRate == null || termMonths == null || termMonths <= 0) {
            return null;
        }

        double monthlyRate = annualInterestRate / 12.0;
        double payment = (amount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -termMonths));

        return BigDecimal.valueOf(payment)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public Flux<Application> getApplicationsForManualReview(int page, int size, String token) {
        System.out.printf("Fetching applications for manual review, page: %d, size: %d%n", page, size);

        return applicationRepository.findAllPendingForReview(page, size)
                .flatMap(application -> {
                    Mono<LoanType> ltMono = loanTypeRepository.findById(application.getLoanType().getId());
                    Mono<Status> stMono = statusRepository.findById(application.getStatus().getId());
                    Mono<User> userMono = authenticationGateway.findByEmail(application.getEmail(), token);

                    return Mono.zip(ltMono, stMono, userMono)
                            .map(tuple -> {
                                LoanType loanType = tuple.getT1();
                                Status status = tuple.getT2();
                                User user = tuple.getT3();

                                Double monthlyPayment = calculateFrenchMonthlyPayment(
                                        application.getAmount(),
                                        loanType.getInterestRate(),
                                        application.getTerm());

                                return application.toBuilder()
                                        .name(user.getFirstName() + " " + user.getLastName())
                                        .baseSalary(user.getBaseSalary())
                                        .loanType(loanType)
                                        .status(status)
                                        .monthlyPayment(monthlyPayment)
                                        .build();
                            });
                });
    }

    public Mono<Application> approve(UUID applicationId, String token) {
        return changeStatus(applicationId, APPROVED_ID, token);
    }

    public Mono<Application> reject(UUID applicationId, String token) {
        return changeStatus(applicationId, REJECTED_ID, token);
    }

    private Mono<Application> changeStatus(UUID applicationId, Integer statusId, String token) {
        return applicationRepository.findById(applicationId)
                .switchIfEmpty(Mono.error(new ApplicationException("REQ_404", "Application not found")))
                .flatMap(app -> {
                    // 🚨 Validar si ya está finalizada
                    if (app.getStatus().getId().equals(APPROVED_ID) || app.getStatus().getId().equals(REJECTED_ID)) {
                        return Mono.error(new ApplicationException("REQ_409",
                                "The application has already been finalized and cannot be modified"));
                    }

                    Mono<LoanType> ltMono = loanTypeRepository.findById(app.getLoanType().getId())
                            .switchIfEmpty(Mono.error(new ApplicationException("REQ_404", "Loan type not found")));
                    Mono<Status> stMono = statusRepository.findById(statusId)
                            .switchIfEmpty(Mono.error(new ApplicationException("REQ_404", "Status not found")));
                    Mono<User> userMono = authenticationGateway.findByEmail(app.getEmail(), token);

                    return Mono.zip(ltMono, stMono, userMono)
                            .map(tuple -> {
                                LoanType loanType = tuple.getT1();
                                Status status = tuple.getT2();
                                User user = tuple.getT3();

                                Double monthlyPayment = calculateFrenchMonthlyPayment(
                                        app.getAmount(),
                                        loanType.getInterestRate(),
                                        app.getTerm());

                                return app.toBuilder()
                                        .loanType(loanType)
                                        .status(status)
                                        .name(user.getFirstName() + " " + user.getLastName())
                                        .baseSalary(user.getBaseSalary())
                                        .monthlyPayment(monthlyPayment)
                                        .build();
                            });
                })
                // ⚠️ usamos el enriquecido para guardar Y para publicar
                .flatMap(appEnriched -> applicationRepository.save(appEnriched)
                        .then(notificationPublisher.publish(appEnriched).thenReturn(appEnriched)));
    }
}
