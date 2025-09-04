package com.mrearsbig.api;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mrearsbig.api.dto.ApplicationRequest;
import com.mrearsbig.api.dto.Response;
import com.mrearsbig.api.helper.ApplicationMapper;
import com.mrearsbig.api.helper.ResponseUtil;
import com.mrearsbig.api.helper.ValidatorUtil;
import com.mrearsbig.usecase.application.ApplicationUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApplicationHandler {
    private final ApplicationUseCase applicationUseCase;
    private final ApplicationMapper mapper;
    private final ValidatorUtil validatorUtil;

    @Operation(
        operationId = "save",
        summary = "Register a new request",
        description = "Register a new request with your basic personal information",
        tags = { "Requests" },
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApplicationRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Successfully registered request",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Response.class) // Response<ApplicationResponse>
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found in authentication service",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Response.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Response.class)
                )
            )
        }
    )
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(ApplicationRequest.class) // 1. Parsear request JSON → DTO
                .flatMap(validatorUtil::validate)
                .map(mapper::toDomain) // 2. Convertir DTO → Dominio
                .flatMap(applicationUseCase::execute) // 3. Llamar al caso de uso
                .map(mapper::toResponse) // 4. Convertir Dominio → Response DTO
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ResponseUtil.responseCreated(response))); // 5. Devolver HTTP 201 con el response
    }
}
