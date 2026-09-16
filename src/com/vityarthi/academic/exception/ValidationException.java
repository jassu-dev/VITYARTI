package com.vityarthi.academic.exception;

/**
 * Thrown when input data validation fails (e.g. invalid GPA, duplicate email, invalid credits).
 */
public class ValidationException extends AcademicException {
    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }
}
