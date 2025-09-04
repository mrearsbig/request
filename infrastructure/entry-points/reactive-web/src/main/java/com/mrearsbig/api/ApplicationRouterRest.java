package com.mrearsbig.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;

@Configuration
public class ApplicationRouterRest {
    @Bean
    @RouterOperation(
        path = "/api/v1/application",
        method = RequestMethod.POST,
        beanClass = ApplicationHandler.class,
        beanMethod = "register",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RouterFunction<ServerResponse> routerFunction(ApplicationHandler handler) {
        return route(POST("/api/v1/application"), handler::save);
    }
}
