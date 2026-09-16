package com.vityarthi.academic.factory;

import com.vityarthi.academic.model.Administrator;
import com.vityarthi.academic.model.Instructor;
import com.vityarthi.academic.model.Role;
import com.vityarthi.academic.model.Student;
import com.vityarthi.academic.model.User;
import com.vityarthi.academic.util.SecurityUtils;

/**
 * Factory Method Pattern: Encapsulates the instantiation logic for polymorphic User subclasses.
 */
public class UserFactory {

    public static User createUser(
            Role role,
            String userId,
            String name,
            String email,
            String rawPassword,
            String param1, // major (Student) / department (Instructor) / adminLevel (Admin)
            String param2  // semester (Student) / designation (Instructor) / officeRoom (Admin)
    ) {
        String passwordHash = SecurityUtils.hashPassword(rawPassword);

        switch (role) {
            case STUDENT:
                int semester = 1;
                try {
                    if (param2 != null) semester = Integer.parseInt(param2);
                } catch (NumberFormatException ignored) {}
                return new Student(userId, name, email, passwordHash, param1 != null ? param1 : "CSE", semester);

            case INSTRUCTOR:
                return new Instructor(userId, name, email, passwordHash,
                        param1 != null ? param1 : "Computer Science",
                        param2 != null ? param2 : "Assistant Professor");

            case ADMIN:
                return new Administrator(userId, name, email, passwordHash,
                        param1 != null ? param1 : "Super Administrator",
                        param2 != null ? param2 : "Office 101");

            default:
                throw new IllegalArgumentException("Unsupported user role: " + role);
        }
    }
}
