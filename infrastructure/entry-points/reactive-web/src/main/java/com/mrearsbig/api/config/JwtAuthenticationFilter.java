package com.mrearsbig.api.config;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.mrearsbig.model.user.gateways.TokenProvider;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter{
    private final TokenProvider tokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String auth = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        String token = auth.substring(7);
        // Aquí puedes agregar la lógica para validar el token JWT
        // Por ejemplo, verificar la firma, la expiración, etc.
        // Si el token es inválido, puedes devolver un error 401
        // Si es válido, puedes extraer la información del usuario y agregarla al contexto de la solicitud
        if (!tokenProvider.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String document = tokenProvider.getUserDocumentFromToken(token);
        exchange.getAttributes().put("document", document);

        String role = tokenProvider.getUserRoleFromToken(token);
        exchange.getAttributes().put("role", role);

        return chain.filter(exchange);
    }
}
