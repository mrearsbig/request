package com.mrearsbig.api.config;

import java.util.List;
import java.util.Set;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrearsbig.api.dto.Response;
import com.mrearsbig.model.config.ApplicationException;
import com.mrearsbig.model.config.ValidationException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    private Mono<Void> buildErrorResponse(ServerWebExchange exchange, HttpStatus status, Response<?> error) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(error);
            var buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Error serializing ErrorResponse", e);
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }

    private Mono<List<String>> toListErrors(Set<ConstraintViolation<?>> violations) {
        return Flux.fromIterable(violations)
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collectList();
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return Mono.just(exchange.getResponse())
                .map(response -> {
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    return response;
                }).flatMap(response -> {
                    if (ex instanceof ValidationException validationEx) {
                        log.warn("Validation error: {}", validationEx.getMessage());
                        return buildErrorResponse(exchange, HttpStatus.CONFLICT,
                                Response.builder()
                                        .code(validationEx.getCode())
                                        .message(validationEx.getMessage())
                                        .build());
                    }

                    if (ex instanceof ApplicationException applicationEx) {
                        log.warn("Application error: {}", applicationEx.getMessage());
                        return buildErrorResponse(exchange, HttpStatus.NOT_FOUND,
                                Response.builder()
                                        .code(applicationEx.getCode())
                                        .message(applicationEx.getMessage())
                                        .build());
                    }

                    if (ex instanceof ConstraintViolationException businessEx) {
                        return toListErrors(businessEx.getConstraintViolations())
                                .flatMap(errors -> {
                                    return buildErrorResponse(exchange, HttpStatus.BAD_REQUEST,
                                            Response.builder()
                                                    .code("REQ_400")
                                                    .message("Validation error")
                                                    .errors(errors)
                                                    .build());
                                });
                    }

                    log.error("Unexpected error", ex);
                    return buildErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR,
                            Response.builder()
                                    .code("GEN_500")
                                    .message("Unexpected error. Please try again later.")
                                    .build());

                });
    }
}
