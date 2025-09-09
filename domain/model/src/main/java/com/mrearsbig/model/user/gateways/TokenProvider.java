package com.mrearsbig.model.user.gateways;

public interface TokenProvider {
    boolean validateToken(String token);
    String getUserDocumentFromToken(String token);
    String getUserRoleFromToken(String token);
}
