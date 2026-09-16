package com.vityarthi.academic.model;

/**
 * Concrete domain model representing a System / Academic Administrator.
 * Demonstrates OOP Inheritance and polymorphism.
 */
public class Administrator extends User {
    private static final long serialVersionUID = 1L;

    private String adminLevel;
    private String officeRoom;

    public Administrator(String userId, String name, String email, String passwordHash, String adminLevel, String officeRoom) {
        super(userId, name, email, passwordHash, Role.ADMIN);
        this.adminLevel = adminLevel != null ? adminLevel : "Senior Registrar";
        this.officeRoom = officeRoom != null ? officeRoom : "Admin Block 204";
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public String getOfficeRoom() {
        return officeRoom;
    }

    public void setOfficeRoom(String officeRoom) {
        this.officeRoom = officeRoom;
    }

    @Override
    public String getRoleSpecificDetails() {
        return String.format("Admin Privilege Level: %s | Office: %s", adminLevel, officeRoom);
    }

    @Override
    public int getMaxCourseAllowance() {
        return Integer.MAX_VALUE; // Unlimited management capability
    }
}
