package com.mrearsbig.r2dbc;

import com.mrearsbig.model.application.Application;
import com.mrearsbig.model.application.gateways.ApplicationRepository;
import com.mrearsbig.model.loantype.LoanType;
import com.mrearsbig.model.status.Status;
import com.mrearsbig.r2dbc.data.ApplicationData;
import com.mrearsbig.r2dbc.helper.ReactiveAdapterOperations;

import reactor.core.publisher.Mono;

import java.util.UUID;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

@Repository
public class ApplicationReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    Application/* change for domain model */,
    ApplicationData/* change for adapter model */,
    UUID,
    ApplicationReactiveRepository
> implements ApplicationRepository {
    private final TransactionalOperator transactionalOperator;

    public ApplicationReactiveRepositoryAdapter(ApplicationReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Application.class/* change for domain model */));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    protected Application toEntity(ApplicationData data) {
        if (data == null) return null;
        return Application.builder()
                .id(data.getId())
                .document(data.getDocument())
                .amount(data.getAmount())
                .term(data.getTerm())
                .email(data.getEmail())
                .loanType(LoanType.builder().id(data.getLoanType()).build()) // 👈 aquí
                .status(Status.builder().id(data.getStatus()).build())       // 👈 aquí
                .build();
    }

    @Override
    protected ApplicationData toData(Application entity) {
        if (entity == null) return null;
        return ApplicationData.builder()
                .id(entity.getId())
                .document(entity.getDocument())
                .amount(entity.getAmount())
                .term(entity.getTerm())
                .email(entity.getEmail())
                .loanType(entity.getLoanType() != null ? entity.getLoanType().getId() : null) // 👈 aquí
                .status(entity.getStatus() != null ? entity.getStatus().getId() : null)       // 👈 aquí
                .build();
    }

    @Override
    public Mono<Application> save(Application application) {
        return super.save(application).as(transactionalOperator::transactional);
    }
}
