package com.mrearsbig.model.application.gateways;

import com.mrearsbig.model.application.Application;

import reactor.core.publisher.Mono;

public interface ApplicationRepository {
    Mono<Application> save(Application application);
}
