package com.mrearsbig.r2dbc;

import com.mrearsbig.model.loantype.LoanType;
import com.mrearsbig.model.loantype.gateways.LoanTypeRepository;
import com.mrearsbig.r2dbc.data.LoanTypeData;
import com.mrearsbig.r2dbc.helper.ReactiveAdapterOperations;

import reactor.core.publisher.Mono;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

@Repository
public class LoanTypeReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    LoanType/* change for domain model */,
    LoanTypeData/* change for adapter model */,
    Integer,
    LoanTypeReactiveRepository
> implements LoanTypeRepository {
    private final TransactionalOperator transactionalOperator;

    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, LoanType.class/* change for domain model */));
        this.transactionalOperator = transactionalOperator;
    }

    public Mono<LoanType> findById(Integer id) {
        return super.findById(id).as(transactionalOperator::transactional);
    }
}
