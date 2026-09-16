package com.vityarthi.academic.service;

import com.vityarthi.academic.exception.CourseFullException;
import com.vityarthi.academic.exception.EntityNotFoundException;
import com.vityarthi.academic.exception.PrerequisiteNotMetException;
import com.vityarthi.academic.exception.ValidationException;
import com.vityarthi.academic.model.Course;
import com.vityarthi.academic.model.Enrollment;
import com.vityarthi.academic.model.Grade;
import com.vityarthi.academic.model.Student;
import com.vityarthi.academic.model.User;
import com.vityarthi.academic.repository.FileCourseRepository;
import com.vityarthi.academic.repository.FileEnrollmentRepository;
import com.vityarthi.academic.repository.FileUserRepository;
import com.vityarthi.academic.strategy.GradingStrategy;
import com.vityarthi.academic.util.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service managing student registration, prerequisite enforcement, capacity bounds, and grading.
 */
public class EnrollmentService {

    private final FileEnrollmentRepository enrollmentRepository;
    private final FileCourseRepository courseRepository;
    private final FileUserRepository userRepository;

    public EnrollmentService(
            FileEnrollmentRepository enrollmentRepository,
            FileCourseRepository courseRepository,
            FileUserRepository userRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public synchronized Enrollment enrollStudent(String studentId, String courseCode)
            throws EntityNotFoundException, CourseFullException, PrerequisiteNotMetException, ValidationException {

        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student", studentId));

        if (!(user instanceof Student student)) {
            throw new ValidationException("User ID " + studentId + " is not a Student.");
        }

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));

        // Check if student is already actively enrolled in this course
        Optional<Enrollment> existing = enrollmentRepository.findByStudentAndCourse(studentId, course.getCourseCode());
        if (existing.isPresent()) {
            throw new ValidationException("Student is already registered for " + course.getCourseCode());
        }

        // Check course capacity
        if (course.isFull()) {
            throw new CourseFullException(course.getCourseCode(), course.getCapacity());
        }

        // Check maximum course allowance
        if (student.getCurrentEnrolledCourseCodes().size() >= student.getMaxCourseAllowance()) {
            throw new ValidationException(String.format("Student has reached maximum course limit of %d courses.",
                    student.getMaxCourseAllowance()));
        }

        // Check prerequisites
        List<String> missingPrereqs = new ArrayList<>();
        for (String prereqCode : course.getPrerequisiteCodes()) {
            if (!student.getCompletedCourseCodes().contains(prereqCode.toUpperCase())) {
                missingPrereqs.add(prereqCode);
            }
        }
        if (!missingPrereqs.isEmpty()) {
            throw new PrerequisiteNotMetException(studentId, course.getCourseCode(), missingPrereqs);
        }

        // Proceed with enrollment
        String enrollmentId = "ENR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Enrollment enrollment = new Enrollment(enrollmentId, student.getUserId(), course.getCourseCode(),
                ConfigManager.getInstance().getCurrentSemester());

        course.addStudent(student.getUserId());
        student.enrollInCourse(course.getCourseCode());

        courseRepository.save(course);
        userRepository.save(student);
        return enrollmentRepository.save(enrollment);
    }

    public synchronized void dropCourse(String studentId, String courseCode)
            throws EntityNotFoundException, ValidationException {

        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        if (!(user instanceof Student student)) {
            throw new ValidationException("User " + studentId + " is not a student.");
        }

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(studentId, courseCode)
                .orElseThrow(() -> new ValidationException("No active registration found for " + courseCode));

        if (enrollment.isCompleted()) {
            throw new ValidationException("Cannot drop a course that has already been graded and completed.");
        }

        enrollment.setStatus(Enrollment.Status.DROPPED);
        student.dropCourse(course.getCourseCode());
        course.removeStudent(student.getUserId());

        courseRepository.save(course);
        userRepository.save(student);
        enrollmentRepository.save(enrollment);
    }

    public synchronized Grade submitGrade(String instructorId, String studentId, String courseCode, double numericScore)
            throws EntityNotFoundException, ValidationException {

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));

        if (!course.getInstructorId().equalsIgnoreCase(instructorId)) {
            throw new ValidationException("Instructor " + instructorId + " is not authorized to grade course " + courseCode);
        }

        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        if (!(user instanceof Student student)) {
            throw new ValidationException("User " + studentId + " is not a student.");
        }

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(studentId, courseCode)
                .orElseThrow(() -> new ValidationException("Active enrollment not found for " + studentId + " in " + courseCode));

        GradingStrategy strategy = ConfigManager.getInstance().getActiveGradingStrategy();
        Grade grade = strategy.evaluate(numericScore);

        enrollment.assignGrade(grade);
        student.dropCourse(course.getCourseCode());

        if (grade.isPassing()) {
            student.markCourseCompleted(course.getCourseCode());
            student.addCompletedCredits(course.getCredits());
        }

        // Recalculate student GPA across all completed enrollments
        recalculateGpa(student);

        enrollmentRepository.save(enrollment);
        userRepository.save(student);
        return grade;
    }

    private void recalculateGpa(Student student) {
        List<Enrollment> completed = enrollmentRepository.findByStudentId(student.getUserId()).stream()
                .filter(Enrollment::isCompleted)
                .toList();

        if (completed.isEmpty()) {
            student.setCumulativeGpa(0.0);
            return;
        }

        double totalGradePointsTimesCredits = 0.0;
        int totalCreditsAttempted = 0;

        for (Enrollment e : completed) {
            Optional<Course> cOpt = courseRepository.findById(e.getCourseCode());
            int credits = cOpt.map(Course::getCredits).orElse(3);
            totalCreditsAttempted += credits;
            totalGradePointsTimesCredits += (e.getGrade().getGradePoint() * credits);
        }

        if (totalCreditsAttempted > 0) {
            student.setCumulativeGpa(totalGradePointsTimesCredits / totalCreditsAttempted);
        }
    }

    public List<Enrollment> getStudentEnrollments(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getCourseEnrollments(String courseCode) {
        return enrollmentRepository.findByCourseCode(courseCode);
    }
}
