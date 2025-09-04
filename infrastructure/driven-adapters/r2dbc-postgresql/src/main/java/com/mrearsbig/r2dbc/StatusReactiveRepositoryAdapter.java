package com.mrearsbig.r2dbc;

import com.mrearsbig.model.status.Status;
import com.mrearsbig.model.status.gateways.StatusRepository;
import com.mrearsbig.r2dbc.data.StatusData;
import com.mrearsbig.r2dbc.helper.ReactiveAdapterOperations;

import reactor.core.publisher.Mono;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

@Repository
public class StatusReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    Status/* change for domain model */,
    StatusData/* change for adapter model */,
    Integer,
    StatusReactiveRepository
> implements StatusRepository {
    private final TransactionalOperator transactionalOperator;

    public StatusReactiveRepositoryAdapter(StatusReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Status.class/* change for domain model */));
        this.transactionalOperator = transactionalOperator;
    }

    public Mono<Status> findById(Integer id) {
        return super.findById(id).as(transactionalOperator::transactional);
    }
}
