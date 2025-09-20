package com.mrearsbig.model.application.gateways;

import java.util.UUID;

import com.mrearsbig.model.application.Application;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicationRepository {
    Mono<Application> findById(UUID id);
    Mono<Application> save(Application application);
    Flux<Application> findAllPendingForReview(int page, int size);
}
