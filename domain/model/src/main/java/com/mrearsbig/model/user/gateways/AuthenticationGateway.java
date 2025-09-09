package com.mrearsbig.model.user.gateways;

import reactor.core.publisher.Mono;

public interface AuthenticationGateway {
    Mono<Boolean> existsByEmailAndDocument(String email, String document, String token);
}
