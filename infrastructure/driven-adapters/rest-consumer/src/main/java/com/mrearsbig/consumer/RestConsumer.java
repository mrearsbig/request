package com.mrearsbig.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mrearsbig.model.user.gateways.AuthenticationGateway;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements AuthenticationGateway {
    private final WebClient client;

    @CircuitBreaker(name = "existsByEmailAndDocument" /* , fallbackMethod = "testGetOk" */)
    public Mono<Boolean> existsByEmailAndDocument(String email, String document) {
        return client
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/users")
                        .queryParam("email", email)
                        .queryParam("document", document)
                        .build())
                .retrieve()
                .bodyToMono(ObjectResponse.class)
                .map(response -> Boolean.TRUE.equals(response.getData()));
    }

}
