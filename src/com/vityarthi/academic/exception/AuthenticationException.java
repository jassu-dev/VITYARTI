package com.vityarthi.academic.exception;

/**
 * Thrown when credentials fail or user account is unauthorized.
 */
public class AuthenticationException extends AcademicException {
    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message);
    }
}
