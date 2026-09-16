package com.vityarthi.academic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Concrete domain model representing a Student.
 * Demonstrates OOP Inheritance and Polymorphic method overrides.
 */
public class Student extends User {
    private static final long serialVersionUID = 1L;

    private String major;
    private int semester;
    private double cumulativeGpa;
    private int completedCredits;
    private final Set<String> completedCourseCodes;
    private final List<String> currentEnrolledCourseCodes;

    public Student(String userId, String name, String email, String passwordHash, String major, int semester) {
        super(userId, name, email, passwordHash, Role.STUDENT);
        this.major = major != null ? major : "Computer Science";
        this.semester = Math.max(semester, 1);
        this.cumulativeGpa = 0.0;
        this.completedCredits = 0;
        this.completedCourseCodes = new HashSet<>();
        this.currentEnrolledCourseCodes = new ArrayList<>();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = Math.max(semester, 1);
    }

    public double getCumulativeGpa() {
        return cumulativeGpa;
    }

    public void setCumulativeGpa(double cumulativeGpa) {
        this.cumulativeGpa = Math.round(cumulativeGpa * 100.0) / 100.0;
    }

    public int getCompletedCredits() {
        return completedCredits;
    }

    public void addCompletedCredits(int credits) {
        this.completedCredits += Math.max(0, credits);
    }

    public Set<String> getCompletedCourseCodes() {
        return Collections.unmodifiableSet(completedCourseCodes);
    }

    public void markCourseCompleted(String courseCode) {
        this.completedCourseCodes.add(courseCode.toUpperCase());
    }

    public List<String> getCurrentEnrolledCourseCodes() {
        return Collections.unmodifiableList(currentEnrolledCourseCodes);
    }

    public void enrollInCourse(String courseCode) {
        if (!currentEnrolledCourseCodes.contains(courseCode.toUpperCase())) {
            currentEnrolledCourseCodes.add(courseCode.toUpperCase());
        }
    }

    public void dropCourse(String courseCode) {
        currentEnrolledCourseCodes.remove(courseCode.toUpperCase());
    }

    @Override
    public String getRoleSpecificDetails() {
        return String.format("Major: %s | Semester: %d | CGPA: %.2f | Completed Credits: %d | Current Courses: %d",
                major, semester, cumulativeGpa, completedCredits, currentEnrolledCourseCodes.size());
    }

    @Override
    public int getMaxCourseAllowance() {
        // Students can register up to 6 courses per term
        return 6;
    }
}
