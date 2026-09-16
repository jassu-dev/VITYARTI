package com.vityarthi.academic.ui;

import com.vityarthi.academic.exception.AcademicException;
import com.vityarthi.academic.model.Administrator;
import com.vityarthi.academic.model.Course;
import com.vityarthi.academic.model.Enrollment;
import com.vityarthi.academic.model.Grade;
import com.vityarthi.academic.model.Instructor;
import com.vityarthi.academic.model.Role;
import com.vityarthi.academic.model.Student;
import com.vityarthi.academic.model.User;
import com.vityarthi.academic.service.AuthenticationService;
import com.vityarthi.academic.service.CourseService;
import com.vityarthi.academic.service.EnrollmentService;
import com.vityarthi.academic.service.ReportService;
import com.vityarthi.academic.test.SystemValidationTest;
import com.vityarthi.academic.util.ConfigManager;

import java.util.List;
import java.util.Scanner;

/**
 * Interactive Console Interface providing role-tailored dashboards.
 */
public class ConsoleUI {

    private final AuthenticationService authService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final ReportService reportService;
    private final Scanner scanner;

    public ConsoleUI(
            AuthenticationService authService,
            CourseService courseService,
            EnrollmentService enrollmentService,
            ReportService reportService
    ) {
        this.authService = authService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.reportService = reportService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            if (!authService.isAuthenticated()) {
                running = renderGuestMenu();
            } else {
                User user = authService.getCurrentUser();
                switch (user.getRole()) {
                    case STUDENT -> renderStudentMenu((Student) user);
                    case INSTRUCTOR -> renderInstructorMenu((Instructor) user);
                    case ADMIN -> renderAdminMenu((Administrator) user);
                }
            }
        }
        System.out.println("\n[+] Thank you for using EduPulse Academic System. Goodbye!");
    }

    private boolean renderGuestMenu() {
        printHeader("EDUPULSE : ACADEMIC MANAGEMENT SYSTEM");
        System.out.println(" 1. User Login (Admin / Faculty / Student)");
        System.out.println(" 2. Browse Public Course Catalog");
        System.out.println(" 3. Run Self-Diagnostics / Automated Test Suite");
        System.out.println(" 4. View Default Demo Credentials");
        System.out.println(" 5. Exit System");
        System.out.print("\nSelect Option [1-5]: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> handleLogin();
            case "2" -> listAllCourses();
            case "3" -> runDiagnostics();
            case "4" -> showDemoCredentials();
            case "5" -> {
                return false;
            }
            default -> System.out.println("[!] Invalid selection. Please enter 1-5.");
        }
        return true;
    }

    private void handleLogin() {
        printHeader("USER AUTHENTICATION");
        System.out.print("Enter User ID or Email: ");
        String identifier = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        try {
            User user = authService.login(identifier, password);
            System.out.printf("\n[SUCCESS] Welcome back, %s! (Role: %s)\n", user.getName(), user.getRole().getDisplayName());
            pause();
        } catch (AcademicException e) {
            System.out.println("\n[ERROR] Authentication failed: " + e.getMessage());
            pause();
        }
    }

    private void renderStudentMenu(Student student) {
        printHeader("STUDENT DASHBOARD | " + student.getName() + " (" + student.getUserId() + ")");
        System.out.println(" 1. View Academic Profile & CGPA");
        System.out.println(" 2. View My Current Enrolled Courses");
        System.out.println(" 3. Register for a New Course");
        System.out.println(" 4. Drop an Enrolled Course");
        System.out.println(" 5. View Official Academic Transcript");
        System.out.println(" 6. Browse Course Catalog & Prerequisites");
        System.out.println(" 7. Logout");
        System.out.print("\nSelect Option [1-7]: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> {
                printHeader("ACADEMIC PROFILE");
                System.out.println("Student Name       : " + student.getName());
                System.out.println("Registration ID    : " + student.getUserId());
                System.out.println("Degree Major       : " + student.getMajor());
                System.out.println("Current Semester   : " + student.getSemester());
                System.out.println("Cumulative GPA     : " + student.getCumulativeGpa());
                System.out.println("Completed Credits  : " + student.getCompletedCredits());
                System.out.println("Completed Courses  : " + student.getCompletedCourseCodes());
                pause();
            }
            case "2" -> {
                printHeader("CURRENT ENROLLMENTS");
                List<Enrollment> list = enrollmentService.getStudentEnrollments(student.getUserId());
                if (list.isEmpty()) {
                    System.out.println("No enrollments found for current semester.");
                } else {
                    list.forEach(System.out::println);
                }
                pause();
            }
            case "3" -> {
                printHeader("REGISTER FOR COURSE");
                listAllCourses();
                System.out.print("\nEnter Course Code to Register (e.g. CSE2001): ");
                String code = scanner.nextLine().trim();
                try {
                    Enrollment e = enrollmentService.enrollStudent(student.getUserId(), code);
                    System.out.println("\n[SUCCESS] Successfully enrolled in " + e.getCourseCode() + "! Enrollment ID: " + e.getEnrollmentId());
                } catch (Exception ex) {
                    System.out.println("\n[REGISTRATION FAILED] " + ex.getMessage());
                }
                pause();
            }
            case "4" -> {
                printHeader("DROP COURSE");
                System.out.print("Enter Course Code to Drop: ");
                String code = scanner.nextLine().trim();
                try {
                    enrollmentService.dropCourse(student.getUserId(), code);
                    System.out.println("\n[SUCCESS] Course " + code + " has been dropped.");
                } catch (Exception ex) {
                    System.out.println("\n[FAILED] " + ex.getMessage());
                }
                pause();
            }
            case "5" -> {
                printHeader("ACADEMIC TRANSCRIPT");
                try {
                    System.out.println(reportService.generateStudentTranscript(student.getUserId()));
                } catch (Exception ex) {
                    System.out.println("[ERROR] " + ex.getMessage());
                }
                pause();
            }
            case "6" -> {
                listAllCourses();
                pause();
            }
            case "7" -> {
                authService.logout();
                System.out.println("\n[+] Logged out successfully.");
                pause();
            }
            default -> System.out.println("[!] Invalid option.");
        }
    }

    private void renderInstructorMenu(Instructor instructor) {
        printHeader("FACULTY DASHBOARD | " + instructor.getName() + " (" + instructor.getUserId() + ")");
        System.out.println(" 1. View My Assigned Courses");
        System.out.println(" 2. View Course Enrolled Students");
        System.out.println(" 3. Evaluate & Submit Student Grade");
        System.out.println(" 4. View Course Performance Analytics");
        System.out.println(" 5. Logout");
        System.out.print("\nSelect Option [1-5]: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> {
                printHeader("ASSIGNED COURSES");
                List<Course> courses = courseService.getCoursesByInstructor(instructor.getUserId());
                if (courses.isEmpty()) {
                    System.out.println("No courses currently assigned to you.");
                } else {
                    courses.forEach(System.out::println);
                }
                pause();
            }
            case "2" -> {
                printHeader("COURSE ROSTER");
                System.out.print("Enter Course Code: ");
                String code = scanner.nextLine().trim();
                List<Enrollment> list = enrollmentService.getCourseEnrollments(code);
                if (list.isEmpty()) {
                    System.out.println("No students enrolled in " + code);
                } else {
                    list.forEach(System.out::println);
                }
                pause();
            }
            case "3" -> {
                printHeader("SUBMIT STUDENT GRADE");
                System.out.print("Enter Course Code: ");
                String code = scanner.nextLine().trim();
                System.out.print("Enter Student Registration ID: ");
                String studentId = scanner.nextLine().trim();
                System.out.print("Enter Numerical Score (0.0 - 100.0): ");
                double score = Double.parseDouble(scanner.nextLine().trim());

                try {
                    Grade g = enrollmentService.submitGrade(instructor.getUserId(), studentId, code, score);
                    System.out.printf("\n[SUCCESS] Grade evaluated & recorded: %s (Points: %.1f)\n",
                            g.getLetterGrade(), g.getGradePoint());
                } catch (Exception ex) {
                    System.out.println("\n[GRADING ERROR] " + ex.getMessage());
                }
                pause();
            }
            case "4" -> {
                printHeader("COURSE ANALYTICS");
                System.out.print("Enter Course Code: ");
                String code = scanner.nextLine().trim();
                try {
                    System.out.println(reportService.generateCourseAnalytics(code));
                } catch (Exception ex) {
                    System.out.println("[ERROR] " + ex.getMessage());
                }
                pause();
            }
            case "5" -> {
                authService.logout();
                System.out.println("\n[+] Logged out successfully.");
                pause();
            }
            default -> System.out.println("[!] Invalid option.");
        }
    }

    private void renderAdminMenu(Administrator admin) {
        printHeader("ADMINISTRATOR CONTROL CONSOLE | " + admin.getName());
        System.out.println(" 1. View System Executive Summary Report");
        System.out.println(" 2. Create & Register New Course");
        System.out.println(" 3. Add Course Prerequisite Rule");
        System.out.println(" 4. Assign Instructor to Course");
        System.out.println(" 5. Register New User (Student/Faculty/Admin)");
        System.out.println(" 6. Toggle Institutional Grading Scheme (Standard 4.0 vs VIT 10-Point)");
        System.out.println(" 7. View Detailed Course Catalog");
        System.out.println(" 8. Logout");
        System.out.print("\nSelect Option [1-8]: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> {
                printHeader("SYSTEM EXECUTIVE REPORT");
                System.out.println(reportService.generateSystemSummaryReport());
                pause();
            }
            case "2" -> {
                printHeader("CREATE NEW COURSE");
                System.out.print("Enter Course Code (e.g. CSE4001): ");
                String code = scanner.nextLine().trim();
                System.out.print("Enter Course Title: ");
                String title = scanner.nextLine().trim();
                System.out.print("Enter Credits [1-6]: ");
                int credits = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter Department: ");
                String dept = scanner.nextLine().trim();
                System.out.print("Enter Max Seat Capacity: ");
                int cap = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter Instructor ID (or leave blank): ");
                String instId = scanner.nextLine().trim();

                try {
                    Course c = courseService.createCourse(code, title, credits, dept, cap, instId.isBlank() ? "UNASSIGNED" : instId);
                    System.out.println("\n[SUCCESS] Course successfully registered: " + c);
                } catch (Exception ex) {
                    System.out.println("\n[ERROR] " + ex.getMessage());
                }
                pause();
            }
            case "3" -> {
                printHeader("ADD PREREQUISITE RULE");
                System.out.print("Enter Target Course Code: ");
                String target = scanner.nextLine().trim();
                System.out.print("Enter Prerequisite Course Code: ");
                String prereq = scanner.nextLine().trim();
                try {
                    courseService.addPrerequisite(target, prereq);
                    System.out.printf("\n[SUCCESS] Set %s as a required prerequisite for %s\n", prereq, target);
                } catch (Exception ex) {
                    System.out.println("\n[ERROR] " + ex.getMessage());
                }
                pause();
            }
            case "4" -> {
                printHeader("ASSIGN INSTRUCTOR");
                System.out.print("Enter Course Code: ");
                String code = scanner.nextLine().trim();
                System.out.print("Enter Faculty User ID: ");
                String instId = scanner.nextLine().trim();
                try {
                    courseService.assignInstructor(code, instId);
                    System.out.printf("\n[SUCCESS] Assigned %s to teach course %s\n", instId, code);
                } catch (Exception ex) {
                    System.out.println("\n[ERROR] " + ex.getMessage());
                }
                pause();
            }
            case "5" -> {
                printHeader("REGISTER NEW USER");
                System.out.println("Role: 1. STUDENT  2. INSTRUCTOR  3. ADMIN");
                System.out.print("Select Role [1-3]: ");
                String rChoice = scanner.nextLine().trim();
                Role role = switch (rChoice) {
                    case "2" -> Role.INSTRUCTOR;
                    case "3" -> Role.ADMIN;
                    default -> Role.STUDENT;
                };

                System.out.print("User ID: ");
                String uid = scanner.nextLine().trim();
                System.out.print("Full Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Email: ");
                String email = scanner.nextLine().trim();
                System.out.print("Password: ");
                String pass = scanner.nextLine().trim();
                System.out.print(role == Role.STUDENT ? "Major: " : (role == Role.INSTRUCTOR ? "Department: " : "Admin Level: "));
                String p1 = scanner.nextLine().trim();
                System.out.print(role == Role.STUDENT ? "Semester [1-8]: " : (role == Role.INSTRUCTOR ? "Designation: " : "Office Room: "));
                String p2 = scanner.nextLine().trim();

                try {
                    User u = authService.registerUser(role, uid, name, email, pass, p1, p2);
                    System.out.println("\n[SUCCESS] Registered user: " + u);
                } catch (Exception ex) {
                    System.out.println("\n[ERROR] " + ex.getMessage());
                }
                pause();
            }
            case "6" -> {
                ConfigManager.getInstance().toggleGradingStrategy();
                System.out.println("\n[SUCCESS] Active Grading Strategy switched to: "
                        + ConfigManager.getInstance().getActiveGradingStrategy().getStrategyName());
                pause();
            }
            case "7" -> {
                listAllCourses();
                pause();
            }
            case "8" -> {
                authService.logout();
                System.out.println("\n[+] Admin logged out.");
                pause();
            }
            default -> System.out.println("[!] Invalid option.");
        }
    }

    private void listAllCourses() {
        printHeader("AVAILABLE COURSE CATALOG");
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses currently in catalog.");
        } else {
            System.out.printf("%-10s %-32s %-8s %-12s %-14s %-12s %s\n",
                    "CODE", "TITLE", "CREDITS", "DEPT", "INSTRUCTOR", "SEATS", "PREREQUISITES");
            System.out.println("-".repeat(95));
            for (Course c : courses) {
                String prereqs = c.getPrerequisiteCodes().isEmpty() ? "None" : String.join(", ", c.getPrerequisiteCodes());
                System.out.printf("%-10s %-32s %-8d %-12s %-14s %-12s %s\n",
                        c.getCourseCode(),
                        c.getCourseTitle().length() > 30 ? c.getCourseTitle().substring(0, 27) + "..." : c.getCourseTitle(),
                        c.getCredits(),
                        c.getDepartment(),
                        c.getInstructorId(),
                        c.getEnrolledStudentIds().size() + "/" + c.getCapacity(),
                        prereqs);
            }
        }
    }

    private void runDiagnostics() {
        printHeader("RUNNING SYSTEM DIAGNOSTICS & VALIDATION TEST SUITE");
        SystemValidationTest.runTests();
        pause();
    }

    private void showDemoCredentials() {
        printHeader("PRE-SEEDED DEMO CREDENTIALS");
        System.out.println("┌────────────┬─────────────┬────────────┬──────────────────────────────────────┐");
        System.out.println("│ Role       │ User ID     │ Password   │ Description                          │");
        System.out.println("├────────────┼─────────────┼────────────┼──────────────────────────────────────┤");
        System.out.println("│ Admin      │ admin       │ admin123   │ System Administrator (Registrar)     │");
        System.out.println("│ Instructor │ prof_smith  │ prof123    │ Dr. Alan Smith (CS Professor)        │");
        System.out.println("│ Instructor │ prof_jones  │ prof123    │ Dr. Sarah Jones (Associate Professor)│");
        System.out.println("│ Student    │ 23BCE0001   │ stu123     │ Aditya Sharma (Sem 3, Has CSE1001)   │");
        System.out.println("│ Student    │ 23BCE0003   │ stu123     │ Rahul Verma (Sem 3, Fresh Enrollee)  │");
        System.out.println("└────────────┴─────────────┴────────────┴──────────────────────────────────────┘");
        pause();
    }

    private void printHeader(String title) {
        System.out.println("\n" + "=".repeat(75));
        System.out.println("  " + title);
        System.out.println("=".repeat(75));
    }

    private void pause() {
        System.out.print("\nPress [Enter] to continue...");
        scanner.nextLine();
    }
}
