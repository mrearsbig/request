package com.mrearsbig.model.loantype.gateways;

import com.mrearsbig.model.loantype.LoanType;

import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<LoanType> findById(Integer id);
}
