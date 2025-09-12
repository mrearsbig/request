package com.mrearsbig.model.user.gateways;

import com.mrearsbig.model.user.User;

import reactor.core.publisher.Mono;

public interface AuthenticationGateway {
    Mono<Boolean> existsByEmailAndDocument(String email, String document, String token);
    Mono<User> findByEmail(String email, String token);
}
