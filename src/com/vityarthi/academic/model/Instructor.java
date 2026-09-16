package com.vityarthi.academic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Concrete domain model representing a Faculty Instructor.
 * Demonstrates OOP Inheritance and polymorphism.
 */
public class Instructor extends User {
    private static final long serialVersionUID = 1L;

    private String department;
    private String designation;
    private final List<String> assignedCourseCodes;

    public Instructor(String userId, String name, String email, String passwordHash, String department, String designation) {
        super(userId, name, email, passwordHash, Role.INSTRUCTOR);
        this.department = department != null ? department : "School of Computing Science";
        this.designation = designation != null ? designation : "Assistant Professor";
        this.assignedCourseCodes = new ArrayList<>();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public List<String> getAssignedCourseCodes() {
        return Collections.unmodifiableList(assignedCourseCodes);
    }

    public void assignCourse(String courseCode) {
        if (!assignedCourseCodes.contains(courseCode.toUpperCase())) {
            assignedCourseCodes.add(courseCode.toUpperCase());
        }
    }

    public void removeCourse(String courseCode) {
        assignedCourseCodes.remove(courseCode.toUpperCase());
    }

    @Override
    public String getRoleSpecificDetails() {
        return String.format("Dept: %s | Designation: %s | Courses Taught: %d",
                department, designation, assignedCourseCodes.size());
    }

    @Override
    public int getMaxCourseAllowance() {
        // Instructors can teach up to 4 courses concurrently
        return 4;
    }
}
