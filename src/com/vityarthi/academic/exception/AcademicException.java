package com.vityarthi.academic.exception;

/**
 * Base custom exception for academic system errors.
 * Demonstrates Java Exception Hierarchy and custom error handling.
 */
public class AcademicException extends Exception {
    private static final long serialVersionUID = 1L;

    public AcademicException(String message) {
        super(message);
    }

    public AcademicException(String message, Throwable cause) {
        super(message, cause);
    }
}
