package com.mrearsbig.model.user.gateways;

public interface TokenProvider {
    /**
     * Validates the given token.
     *
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);

    /**
     * Extracts the user document from the given token.
     *
     * @param token the token from which to extract the user document
     * @return the user document as a String
     */
    String getUserDocumentFromToken(String token);

    /**
     * Extracts the user role from the given token.
     *
     * @param token the token from which to extract the user role
     * @return the user role as a String
     */
    String getUserRoleFromToken(String token);
}
