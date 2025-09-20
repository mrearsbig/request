package com.mrearsbig.model.application.gateways;

import com.mrearsbig.model.application.Application;

import reactor.core.publisher.Mono;

public interface NotificationPublisher {
    Mono<Void> publish(Application application);
}
