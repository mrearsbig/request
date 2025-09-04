package com.mrearsbig.model.status.gateways;

import com.mrearsbig.model.status.Status;

import reactor.core.publisher.Mono;

public interface StatusRepository {
    Mono<Status> findById(Integer id);
}
