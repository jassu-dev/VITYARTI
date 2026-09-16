package com.vityarthi.academic;

import com.vityarthi.academic.repository.FileCourseRepository;
import com.vityarthi.academic.repository.FileEnrollmentRepository;
import com.vityarthi.academic.repository.FileUserRepository;
import com.vityarthi.academic.service.AuthenticationService;
import com.vityarthi.academic.service.CourseService;
import com.vityarthi.academic.service.EnrollmentService;
import com.vityarthi.academic.service.ReportService;
import com.vityarthi.academic.test.SystemValidationTest;
import com.vityarthi.academic.ui.ConsoleUI;
import com.vityarthi.academic.util.DataInitializer;

/**
 * Main Application Entry Point for EduPulse Academic & Course Management System.
 */
public class Main {

    public static void main(String[] args) {
        // Support CLI test flag for automated testing
        if (args.length > 0 && "--test".equalsIgnoreCase(args[0])) {
            SystemValidationTest.runTests();
            return;
        }

        // Initialize Persistence Repositories
        FileUserRepository userRepository = new FileUserRepository();
        FileCourseRepository courseRepository = new FileCourseRepository();
        FileEnrollmentRepository enrollmentRepository = new FileEnrollmentRepository();

        // Seed sample benchmark academic data if fresh run
        DataInitializer.seedSampleDataIfEmpty(userRepository, courseRepository, enrollmentRepository);

        // Initialize Business Logic Layer
        AuthenticationService authService = new AuthenticationService(userRepository);
        CourseService courseService = new CourseService(courseRepository, userRepository);
        EnrollmentService enrollmentService = new EnrollmentService(enrollmentRepository, courseRepository, userRepository);
        ReportService reportService = new ReportService(userRepository, courseRepository, enrollmentRepository);

        // Launch Console User Interface
        ConsoleUI ui = new ConsoleUI(authService, courseService, enrollmentService, reportService);
        ui.start();
    }
}
