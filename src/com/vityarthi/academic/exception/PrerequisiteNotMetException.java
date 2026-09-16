package com.vityarthi.academic.exception;

import java.util.List;

/**
 * Thrown when a student attempts to enroll in a course without passing prerequisite courses.
 */
public class PrerequisiteNotMetException extends AcademicException {
    private static final long serialVersionUID = 1L;

    public PrerequisiteNotMetException(String studentId, String courseCode, List<String> missingPrereqs) {
        super(String.format("Student %s cannot register for %s. Missing required prerequisites: %s",
                studentId, courseCode, String.join(", ", missingPrereqs)));
    }
}
