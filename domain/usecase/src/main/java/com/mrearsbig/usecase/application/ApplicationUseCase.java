package com.mrearsbig.usecase.application;

import com.mrearsbig.model.application.Application;
import com.mrearsbig.model.application.gateways.ApplicationRepository;
import com.mrearsbig.model.config.ApplicationException;
import com.mrearsbig.model.loantype.LoanType;
import com.mrearsbig.model.loantype.gateways.LoanTypeRepository;
import com.mrearsbig.model.status.Status;
import com.mrearsbig.model.status.gateways.StatusRepository;
import com.mrearsbig.model.user.gateways.AuthenticationGateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class ApplicationUseCase {
    private final ApplicationRepository applicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StatusRepository statusRepository;
    private final AuthenticationGateway authenticationGateway;

    private static final Integer PENDING_REVIEW_ID = 2;

    public Mono<Application> execute(Application application, String token) {
        log.info("Start execute application: {}", application);

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

}
