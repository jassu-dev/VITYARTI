package com.vityarthi.academic.util;

import com.vityarthi.academic.factory.UserFactory;
import com.vityarthi.academic.model.Course;
import com.vityarthi.academic.model.Role;
import com.vityarthi.academic.model.Student;
import com.vityarthi.academic.model.User;
import com.vityarthi.academic.repository.FileCourseRepository;
import com.vityarthi.academic.repository.FileEnrollmentRepository;
import com.vityarthi.academic.repository.FileUserRepository;
import com.vityarthi.academic.service.EnrollmentService;

/**
 * Initializes mock domain data for demonstrations, testing, and evaluation.
 */
public class DataInitializer {

    public static void seedSampleDataIfEmpty(
            FileUserRepository userRepo,
            FileCourseRepository courseRepo,
            FileEnrollmentRepository enrollRepo
    ) {
        if (userRepo.count() > 0 && courseRepo.count() > 0) {
            return; // Data already exists
        }

        System.out.println(">> Seeding initial benchmark academic records...");

        // 1. Seed Administrators
        User admin = UserFactory.createUser(Role.ADMIN, "admin", "System Administrator",
                "admin@vityarthi.edu", "admin123", "Chief Registrar", "Room 101");
        userRepo.save(admin);

        // 2. Seed Instructors
        User inst1 = UserFactory.createUser(Role.INSTRUCTOR, "prof_smith", "Dr. Alan Smith",
                "alan.smith@vityarthi.edu", "prof123", "School of Computing Science", "Professor");
        User inst2 = UserFactory.createUser(Role.INSTRUCTOR, "prof_jones", "Dr. Sarah Jones",
                "sarah.jones@vityarthi.edu", "prof123", "Department of Software Engineering", "Associate Professor");
        userRepo.save(inst1);
        userRepo.save(inst2);

        // 3. Seed Students
        User s1 = UserFactory.createUser(Role.STUDENT, "23BCE0001", "Aditya Sharma",
                "aditya.s@vityarthi.edu", "stu123", "B.Tech Computer Science", "3");
        User s2 = UserFactory.createUser(Role.STUDENT, "23BCE0002", "Priya Patel",
                "priya.p@vityarthi.edu", "stu123", "B.Tech AI & Data Science", "3");
        User s3 = UserFactory.createUser(Role.STUDENT, "23BCE0003", "Rahul Verma",
                "rahul.v@vityarthi.edu", "stu123", "B.Tech Computer Science", "3");
        userRepo.save(s1);
        userRepo.save(s2);
        userRepo.save(s3);

        // Pre-mark introductory course completed for student 1 & 2
        ((Student) s1).markCourseCompleted("CSE1001");
        ((Student) s1).addCompletedCredits(3);
        ((Student) s2).markCourseCompleted("CSE1001");
        ((Student) s2).addCompletedCredits(3);
        userRepo.save(s1);
        userRepo.save(s2);

        // 4. Seed Courses
        Course c1 = new Course("CSE1001", "Problem Solving & Programming", 3, "SCSE", 40, "prof_smith");
        Course c2 = new Course("CSE2001", "Object Oriented Programming (Java)", 4, "SCSE", 3, "prof_smith"); // Small cap for testing
        c2.addPrerequisite("CSE1001");

        Course c3 = new Course("CSE2002", "Data Structures & Algorithms", 4, "SCSE", 30, "prof_jones");
        c3.addPrerequisite("CSE1001");

        Course c4 = new Course("CSE3001", "Database Management Systems", 3, "SCSE", 30, "prof_jones");
        c4.addPrerequisite("CSE2002");

        Course c5 = new Course("CSE3002", "Software Engineering Principles", 3, "SCSE", 25, "prof_smith");
        c5.addPrerequisite("CSE2001");

        courseRepo.save(c1);
        courseRepo.save(c2);
        courseRepo.save(c3);
        courseRepo.save(c4);
        courseRepo.save(c5);

        // 5. Seed sample enrollments and grades
        EnrollmentService enrollmentService = new EnrollmentService(enrollRepo, courseRepo, userRepo);
        try {
            // Student 1 enrolls in CSE2001 & CSE2002
            enrollmentService.enrollStudent("23BCE0001", "CSE2001");
            enrollmentService.enrollStudent("23BCE0001", "CSE2002");

            // Student 2 enrolls in CSE2001
            enrollmentService.enrollStudent("23BCE0002", "CSE2001");

            // Instructor grades student 1 in CSE2001
            enrollmentService.submitGrade("prof_smith", "23BCE0001", "CSE2001", 94.5);
        } catch (Exception e) {
            System.err.println("Seed error: " + e.getMessage());
        }

        System.out.println(">> Benchmark academic records seeded successfully!");
    }
}
