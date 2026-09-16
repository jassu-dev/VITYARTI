package com.vityarthi.academic.exception;

/**
 * Thrown when requested entity (User, Course, Enrollment) is not found in the repository.
 */
public class EntityNotFoundException extends AcademicException {
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String entityType, String identifier) {
        super(String.format("%s with identifier '%s' was not found.", entityType, identifier));
    }
}
