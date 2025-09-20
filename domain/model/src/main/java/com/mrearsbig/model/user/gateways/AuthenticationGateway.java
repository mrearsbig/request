package com.mrearsbig.model.user.gateways;

import com.mrearsbig.model.user.User;

import reactor.core.publisher.Mono;

public interface AuthenticationGateway {
    /**
     * Verifica si existe un usuario en el MS de autenticación
     * a partir del email y documento.
     *
     * @param email correo del usuario
     * @param document documento del usuario
     * @param token JWT usado para autenticar la petición
     * @return Mono<Boolean> true si existe, false en caso contrario
     */
    Mono<Boolean> existsByEmailAndDocument(String email, String document, String token);

    /**
     * Busca un usuario en el MS de autenticación a partir del email.
     *
     * @param email correo del usuario
     * @param token JWT usado para autenticar la petición
     * @return Mono<User> usuario encontrado
     */
    Mono<User> findByEmail(String email, String token);
}
