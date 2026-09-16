package com.vityarthi.academic.exception;

/**
 * Thrown when a course has reached maximum seat capacity during registration.
 */
public class CourseFullException extends AcademicException {
    private static final long serialVersionUID = 1L;

    public CourseFullException(String courseCode, int capacity) {
        super(String.format("Course [%s] is full! Maximum capacity of %d reached.", courseCode, capacity));
    }
}
