package com.vityarthi.academic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Domain model representing a Course.
 * Highlights Encapsulation, invariant protection, and defensive copying.
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseCode;
    private String courseTitle;
    private int credits;
    private String department;
    private int capacity;
    private String instructorId;
    private final List<String> prerequisiteCodes;
    private final List<String> enrolledStudentIds;

    public Course(String courseCode, String courseTitle, int credits, String department, int capacity, String instructorId) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty.");
        }
        if (credits < 1 || credits > 6) {
            throw new IllegalArgumentException("Course credits must be between 1 and 6.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Course capacity must be greater than zero.");
        }

        this.courseCode = courseCode.trim().toUpperCase();
        this.courseTitle = courseTitle != null ? courseTitle.trim() : "Untitled Course";
        this.credits = credits;
        this.department = department != null ? department.trim() : "General";
        this.capacity = capacity;
        this.instructorId = instructorId != null ? instructorId.trim() : "UNASSIGNED";
        this.prerequisiteCodes = new ArrayList<>();
        this.enrolledStudentIds = new ArrayList<>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        if (courseTitle != null && !courseTitle.trim().isEmpty()) {
            this.courseTitle = courseTitle.trim();
        }
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        if (credits >= 1 && credits <= 6) {
            this.credits = credits;
        }
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity >= enrolledStudentIds.size()) {
            this.capacity = capacity;
        }
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId != null ? instructorId.trim() : "UNASSIGNED";
    }

    public List<String> getPrerequisiteCodes() {
        return Collections.unmodifiableList(prerequisiteCodes);
    }

    public void addPrerequisite(String prereqCode) {
        String code = prereqCode.trim().toUpperCase();
        if (!prerequisiteCodes.contains(code) && !code.equals(this.courseCode)) {
            prerequisiteCodes.add(code);
        }
    }

    public List<String> getEnrolledStudentIds() {
        return Collections.unmodifiableList(enrolledStudentIds);
    }

    public boolean isFull() {
        return enrolledStudentIds.size() >= capacity;
    }

    public int getAvailableSeats() {
        return Math.max(0, capacity - enrolledStudentIds.size());
    }

    public boolean addStudent(String studentId) {
        if (isFull() || enrolledStudentIds.contains(studentId)) {
            return false;
        }
        return enrolledStudentIds.add(studentId);
    }

    public boolean removeStudent(String studentId) {
        return enrolledStudentIds.remove(studentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return Objects.equals(courseCode, course.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%d Credits) | Dept: %s | Instructor: %s | Seats: %d/%d",
                courseCode, courseTitle, credits, department, instructorId, enrolledStudentIds.size(), capacity);
    }
}
