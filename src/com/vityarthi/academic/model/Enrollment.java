package com.vityarthi.academic.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Domain entity representing a Student course registration / enrollment.
 */
public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status {
        ENROLLED,
        COMPLETED,
        DROPPED
    }

    private final String enrollmentId;
    private final String studentId;
    private final String courseCode;
    private final String semester;
    private final LocalDate enrollmentDate;
    private Status status;
    private Grade grade;

    public Enrollment(String enrollmentId, String studentId, String courseCode, String semester) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseCode = courseCode.toUpperCase();
        this.semester = semester;
        this.enrollmentDate = LocalDate.now();
        this.status = Status.ENROLLED;
        this.grade = null;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getSemester() {
        return semester;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Grade getGrade() {
        return grade;
    }

    public void assignGrade(Grade grade) {
        this.grade = grade;
        this.status = Status.COMPLETED;
    }

    public boolean isCompleted() {
        return status == Status.COMPLETED && grade != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment that)) return false;
        return Objects.equals(enrollmentId, that.enrollmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId);
    }

    @Override
    public String toString() {
        String gradeStr = (grade != null) ? grade.toString() : "PENDING";
        return String.format("Enrollment[%s]: Student=%s, Course=%s, Term=%s, Status=%s, Grade=%s",
                enrollmentId, studentId, courseCode, semester, status, gradeStr);
    }
}
