package com.vityarthi.academic.test;

import com.vityarthi.academic.exception.CourseFullException;
import com.vityarthi.academic.exception.PrerequisiteNotMetException;
import com.vityarthi.academic.exception.ValidationException;
import com.vityarthi.academic.factory.UserFactory;
import com.vityarthi.academic.model.Course;
import com.vityarthi.academic.model.Grade;
import com.vityarthi.academic.model.Role;
import com.vityarthi.academic.model.Student;
import com.vityarthi.academic.model.User;
import com.vityarthi.academic.repository.FileCourseRepository;
import com.vityarthi.academic.repository.FileEnrollmentRepository;
import com.vityarthi.academic.repository.FileUserRepository;
import com.vityarthi.academic.service.CourseService;
import com.vityarthi.academic.service.EnrollmentService;
import com.vityarthi.academic.strategy.GradingStrategy;
import com.vityarthi.academic.strategy.HonorsGradingStrategy;
import com.vityarthi.academic.strategy.StandardGradingStrategy;
import com.vityarthi.academic.util.SecurityUtils;

import java.io.File;

/**
 * Automated System Validation and Unit Test Runner.
 * Validates domain rules, invariant enforcement, OOP designs, and error handling.
 */
public class SystemValidationTest {

    private static int testsRun = 0;
    private static int testsPassed = 0;

    public static void main(String[] args) {
        runTests();
    }

    public static void runTests() {
        testsRun = 0;
        testsPassed = 0;
        System.out.println("\n------------------------------------------------------------");
        System.out.println("  STARTING AUTOMATED UNIT & INTEGRATION TEST SUITE");
        System.out.println("------------------------------------------------------------");

        testUserFactoryAndPolymorphism();
        testSecurityHashing();
        testGradingStrategyPattern();
        testCoursePrerequisiteEnforcement();
        testCourseCapacityConstraint();
        testDuplicateRegistrationPrevention();
        testDropCourseLifecycle();
        testGpaCalculationAccuracy();

        System.out.println("\n------------------------------------------------------------");
        System.out.printf("  TEST EXECUTION SUMMARY: %d / %d PASSED (%.1f%% SUCCESS)\n",
                testsPassed, testsRun, (testsPassed * 100.0) / testsRun);
        System.out.println("------------------------------------------------------------\n");
    }

    private static void assertTrue(String testName, boolean condition) {
        testsRun++;
        if (condition) {
            testsPassed++;
            System.out.printf("  [PASS] %s\n", testName);
        } else {
            System.err.printf("  [FAIL] %s\n", testName);
        }
    }

    private static void testUserFactoryAndPolymorphism() {
        User student = UserFactory.createUser(Role.STUDENT, "TEST_S1", "Test Student",
                "test@vityarthi.edu", "pass123", "B.Tech IT", "2");
        User instructor = UserFactory.createUser(Role.INSTRUCTOR, "TEST_I1", "Prof. Tester",
                "prof@vityarthi.edu", "pass123", "Computing", "Professor");

        boolean isStudentType = student instanceof Student;
        boolean maxCourseCheck = student.getMaxCourseAllowance() == 6 && instructor.getMaxCourseAllowance() == 4;

        assertTrue("UserFactory creates polymorphic subtype correctly", isStudentType);
        assertTrue("Polymorphic method getMaxCourseAllowance() returns role-specific capacity", maxCourseCheck);
    }

    private static void testSecurityHashing() {
        String raw = "SecurePass2026";
        String hash = SecurityUtils.hashPassword(raw);

        boolean verifyOk = SecurityUtils.verifyPassword(raw, hash);
        boolean verifyBad = !SecurityUtils.verifyPassword("WrongPass", hash);

        assertTrue("SHA-256 password hash verifies matching credential", verifyOk);
        assertTrue("SHA-256 password hash rejects incorrect credential", verifyBad);
    }

    private static void testGradingStrategyPattern() {
        GradingStrategy std = new StandardGradingStrategy();
        GradingStrategy hon = new HonorsGradingStrategy();

        Grade gStd = std.evaluate(92.0);
        Grade gHon = hon.evaluate(92.0);

        boolean stdOk = "A+".equals(gStd.getLetterGrade()) && gStd.getGradePoint() == 4.0;
        boolean honOk = "S".equals(gHon.getLetterGrade()) && gHon.getGradePoint() == 10.0;

        assertTrue("StandardGradingStrategy correctly maps 92% to A+ (4.0)", stdOk);
        assertTrue("HonorsGradingStrategy correctly maps 92% to S (10.0)", honOk);
    }

    private static void testCoursePrerequisiteEnforcement() {
        String tmpDir = "data/test_repo_" + System.currentTimeMillis();
        FileUserRepository uRepo = new FileUserRepository(tmpDir + "/u.dat");
        FileCourseRepository cRepo = new FileCourseRepository(tmpDir + "/c.dat");
        FileEnrollmentRepository eRepo = new FileEnrollmentRepository(tmpDir + "/e.dat");

        Student s = (Student) UserFactory.createUser(Role.STUDENT, "S_PREREQ", "Prereq Student",
                "prereq@vityarthi.edu", "p123", "CSE", "2");
        uRepo.save(s);

        Course cBasic = new Course("CS101", "Basics", 3, "CS", 10, "NONE");
        Course cAdv = new Course("CS201", "Advanced", 4, "CS", 10, "NONE");
        cAdv.addPrerequisite("CS101");

        cRepo.save(cBasic);
        cRepo.save(cAdv);

        EnrollmentService service = new EnrollmentService(eRepo, cRepo, uRepo);
        boolean caughtException = false;
        try {
            service.enrollStudent("S_PREREQ", "CS201");
        } catch (PrerequisiteNotMetException e) {
            caughtException = true;
        } catch (Exception ignored) {}

        assertTrue("Prerequisite enforcement blocks enrollment when prerequisite course missing", caughtException);
        cleanupDir(new File(tmpDir));
    }

    private static void testCourseCapacityConstraint() {
        String tmpDir = "data/test_cap_" + System.currentTimeMillis();
        FileUserRepository uRepo = new FileUserRepository(tmpDir + "/u.dat");
        FileCourseRepository cRepo = new FileCourseRepository(tmpDir + "/c.dat");
        FileEnrollmentRepository eRepo = new FileEnrollmentRepository(tmpDir + "/e.dat");

        Student s1 = (Student) UserFactory.createUser(Role.STUDENT, "S1", "Student One", "s1@v.edu", "p", "CS", "1");
        Student s2 = (Student) UserFactory.createUser(Role.STUDENT, "S2", "Student Two", "s2@v.edu", "p", "CS", "1");
        uRepo.save(s1);
        uRepo.save(s2);

        Course cSmall = new Course("MINI101", "Mini Class", 3, "CS", 1, "NONE"); // Capacity = 1
        cRepo.save(cSmall);

        EnrollmentService service = new EnrollmentService(eRepo, cRepo, uRepo);
        boolean capException = false;
        try {
            service.enrollStudent("S1", "MINI101");
            service.enrollStudent("S2", "MINI101"); // Exceeds cap
        } catch (CourseFullException e) {
            capException = true;
        } catch (Exception ignored) {}

        assertTrue("Capacity constraint prevents enrollment beyond course capacity limit", capException);
        cleanupDir(new File(tmpDir));
    }

    private static void testDuplicateRegistrationPrevention() {
        String tmpDir = "data/test_dup_" + System.currentTimeMillis();
        FileUserRepository uRepo = new FileUserRepository(tmpDir + "/u.dat");
        FileCourseRepository cRepo = new FileCourseRepository(tmpDir + "/c.dat");
        FileEnrollmentRepository eRepo = new FileEnrollmentRepository(tmpDir + "/e.dat");

        Student s = (Student) UserFactory.createUser(Role.STUDENT, "SDUP", "Dup Student", "dup@v.edu", "p", "CS", "1");
        uRepo.save(s);

        Course c = new Course("DUP101", "Test Course", 3, "CS", 10, "NONE");
        cRepo.save(c);

        EnrollmentService service = new EnrollmentService(eRepo, cRepo, uRepo);
        boolean duplicateRejected = false;
        try {
            service.enrollStudent("SDUP", "DUP101");
            service.enrollStudent("SDUP", "DUP101"); // Second enrollment
        } catch (ValidationException e) {
            duplicateRejected = true;
        } catch (Exception ignored) {}

        assertTrue("Duplicate course registration rejected with ValidationException", duplicateRejected);
        cleanupDir(new File(tmpDir));
    }

    private static void testDropCourseLifecycle() {
        String tmpDir = "data/test_drop_" + System.currentTimeMillis();
        FileUserRepository uRepo = new FileUserRepository(tmpDir + "/u.dat");
        FileCourseRepository cRepo = new FileCourseRepository(tmpDir + "/c.dat");
        FileEnrollmentRepository eRepo = new FileEnrollmentRepository(tmpDir + "/e.dat");

        Student s = (Student) UserFactory.createUser(Role.STUDENT, "SDROP", "Drop Student", "drop@v.edu", "p", "CS", "1");
        uRepo.save(s);

        Course c = new Course("DRP101", "Drop Test Course", 3, "CS", 10, "NONE");
        cRepo.save(c);

        EnrollmentService service = new EnrollmentService(eRepo, cRepo, uRepo);
        try {
            service.enrollStudent("SDROP", "DRP101");
            boolean enrolled = cRepo.findById("DRP101").get().getEnrolledStudentIds().contains("SDROP");

            service.dropCourse("SDROP", "DRP101");
            boolean dropped = !cRepo.findById("DRP101").get().getEnrolledStudentIds().contains("SDROP");

            assertTrue("Course drop frees seat in Course and updates enrollment status", enrolled && dropped);
        } catch (Exception e) {
            assertTrue("Course drop lifecycle test passed", false);
        } finally {
            cleanupDir(new File(tmpDir));
        }
    }

    private static void testGpaCalculationAccuracy() {
        String tmpDir = "data/test_gpa_" + System.currentTimeMillis();
        FileUserRepository uRepo = new FileUserRepository(tmpDir + "/u.dat");
        FileCourseRepository cRepo = new FileCourseRepository(tmpDir + "/c.dat");
        FileEnrollmentRepository eRepo = new FileEnrollmentRepository(tmpDir + "/e.dat");

        Student s = (Student) UserFactory.createUser(Role.STUDENT, "SGPA", "GPA Student", "gpa@v.edu", "p", "CS", "1");
        uRepo.save(s);

        User inst = UserFactory.createUser(Role.INSTRUCTOR, "INST_GPA", "Prof. GPA", "gpa_prof@v.edu", "p", "CS", "Prof");
        uRepo.save(inst);

        Course c1 = new Course("GPA1", "Course 1", 3, "CS", 10, "INST_GPA");
        Course c2 = new Course("GPA2", "Course 2", 4, "CS", 10, "INST_GPA");
        cRepo.save(c1);
        cRepo.save(c2);

        EnrollmentService service = new EnrollmentService(eRepo, cRepo, uRepo);
        try {
            service.enrollStudent("SGPA", "GPA1");
            service.enrollStudent("SGPA", "GPA2");

            // Under Honors Scale: 95% = S (10.0), 82% = A (9.0)
            // Expected GPA = (10.0*3 + 9.0*4) / (3+4) = (30 + 36) / 7 = 66 / 7 = 9.43
            service.submitGrade("INST_GPA", "SGPA", "GPA1", 95.0);
            service.submitGrade("INST_GPA", "SGPA", "GPA2", 82.0);

            Student updated = (Student) uRepo.findById("SGPA").get();
            boolean gpaCorrect = Math.abs(updated.getCumulativeGpa() - 9.43) < 0.05;

            assertTrue("Weighted GPA calculated accurately across multiple credit courses", gpaCorrect);
        } catch (Exception e) {
            assertTrue("GPA calculation failed with error: " + e.getMessage(), false);
        } finally {
            cleanupDir(new File(tmpDir));
        }
    }

    private static void cleanupDir(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) f.delete();
            }
            dir.delete();
        }
    }
}
