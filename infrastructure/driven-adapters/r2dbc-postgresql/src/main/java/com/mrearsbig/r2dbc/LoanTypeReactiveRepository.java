package com.mrearsbig.r2dbc;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mrearsbig.r2dbc.data.LoanTypeData;

// TODO: This file is just an example, you should delete or modify it
public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeData, Integer>, ReactiveQueryByExampleExecutor<LoanTypeData> {

}
