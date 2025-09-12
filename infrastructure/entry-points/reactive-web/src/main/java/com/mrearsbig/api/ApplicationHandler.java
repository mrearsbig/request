package com.mrearsbig.api;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
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

        @Operation(operationId = "save", summary = "Register a new application", description = "Register a new request with your basic personal information", tags = {
                        "Requests" }, requestBody = @RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApplicationRequest.class))), responses = {
                                        @ApiResponse(responseCode = "201", description = "Successfully registered request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Response.class) // Response<ApplicationResponse>
                                        )),
                                        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Response.class))),
                                        @ApiResponse(responseCode = "404", description = "User not found in authentication service", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Response.class))),
                                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Response.class)))
                        })
        public Mono<ServerResponse> save(ServerRequest serverRequest) {
                String documentToken = serverRequest.exchange().getAttribute("document");
                String roleToken = serverRequest.exchange().getAttribute("role");
                String rawToken = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION);

                if (documentToken == null || roleToken == null || rawToken == null) {
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(ResponseUtil
                                                        .responseError("Unauthorized: Missing or invalid token"));
                }

                if (!"CLIENT".equalsIgnoreCase(roleToken)) {
                        return ServerResponse.status(HttpStatus.FORBIDDEN)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(ResponseUtil
                                                        .responseError("Only CLIENT role can create loan requests"));
                }

                return serverRequest.bodyToMono(ApplicationRequest.class)
                                .flatMap(validatorUtil::validate)
                                .flatMap(request -> {
                                        if (!request.getDocument().equals(documentToken)) {
                                                return ServerResponse.status(HttpStatus.FORBIDDEN)
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .bodyValue(ResponseUtil.responseError(
                                                                                "Unauthorized: Document does not match token"));
                                        }

                                        return applicationUseCase
                                                        .execute(mapper.toDomain(request),
                                                                        rawToken.replace("Bearer ", ""))
                                                        .map(mapper::toResponse)
                                                        .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                        .bodyValue(ResponseUtil
                                                                                        .responseCreated(response)));
                                });
        }

        @Operation(operationId = "findAllPendingForReview", summary = "List applications pending for manual review", description = "Returns a paginated list of loan applications that need manual review", tags = {
                        "Requests" }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Successfully retrieved applications", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Response.class))),
                                        @ApiResponse(responseCode = "403", description = "Forbidden - Only ASESOR role can access this endpoint", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Response.class))),
                                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Response.class)))
                        })
        public Mono<ServerResponse> findAllPendingForReview(ServerRequest serverRequest) {
                String roleToken = serverRequest.exchange().getAttribute("role");
                String rawToken = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION);

                if (roleToken == null || rawToken == null) {
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(ResponseUtil
                                                        .responseError("Unauthorized: Missing or invalid token"));
                }

                if (!"ADVISOR".equalsIgnoreCase(roleToken)) {
                        return ServerResponse.status(HttpStatus.FORBIDDEN)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(ResponseUtil
                                                        .responseError("Only ADVISOR role can access this endpoint"));
                }

                int page = serverRequest.queryParam("page")
                                .map(Integer::parseInt)
                                .orElse(0);
                int size = serverRequest.queryParam("size")
                                .map(Integer::parseInt)
                                .orElse(10);

                return applicationUseCase.getApplicationsForManualReview(page, size, rawToken.replace("Bearer ", ""))
                                .map(mapper::toReviewResponse) // 👈 necesitas este método en tu ApplicationMapper
                                .collectList()
                                .flatMap(responses -> ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(ResponseUtil.responseList(responses)));
        }

}
