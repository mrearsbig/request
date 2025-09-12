package com.mrearsbig.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

@Configuration
public class ApplicationRouterRest {
    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/v1/application",
            method = RequestMethod.POST,
            beanClass = ApplicationHandler.class,
            beanMethod = "save",
            produces = MediaType.APPLICATION_JSON_VALUE
        ),
        @RouterOperation(
            path = "/api/v1/application",
            method = RequestMethod.GET,
            beanClass = ApplicationHandler.class,
            beanMethod = "findAllPendingForReview",
            produces = MediaType.APPLICATION_JSON_VALUE
        )
    })
    public RouterFunction<ServerResponse> routerFunction(ApplicationHandler handler) {
        return route(POST("/api/v1/application"), handler::save)
             .andRoute(GET("/api/v1/application"), handler::findAllPendingForReview);
    }
}
