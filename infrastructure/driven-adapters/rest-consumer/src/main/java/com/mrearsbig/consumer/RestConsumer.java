package com.mrearsbig.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mrearsbig.model.user.User;
import com.mrearsbig.model.user.gateways.AuthenticationGateway;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements AuthenticationGateway {
    private final WebClient client;

    @CircuitBreaker(name = "existsByEmailAndDocument" /* , fallbackMethod = "testGetOk" */)
    public Mono<Boolean> existsByEmailAndDocument(String email, String document, String token) {
        return client
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/users")
                        .queryParam("email", email)
                        .queryParam("document", document)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) // 🔑 propaga el token del usuario
                .retrieve()
                .bodyToMono(ObjectResponse.class)
                .map(response -> Boolean.TRUE.equals(response.getData()));
    }

    @CircuitBreaker(name = "findByEmail" /* , fallbackMethod = "testGetOk" */)
    public Mono<User> findByEmail(String email, String token) {
        return client
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/users/email")
                        .queryParam("email", email)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(User.class);
    }
}
