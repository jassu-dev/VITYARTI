package com.vityarthi.academic.model;

/**
 * Enumeration representing user security and access roles in EduPulse.
 */
public enum Role {
    ADMIN("Administrator", 1),
    INSTRUCTOR("Faculty / Instructor", 2),
    STUDENT("Student", 3);

    private final String displayName;
    private final int accessLevel;

    Role(String displayName, int accessLevel) {
        this.displayName = displayName;
        this.accessLevel = accessLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getAccessLevel() {
        return accessLevel;
    }
}
