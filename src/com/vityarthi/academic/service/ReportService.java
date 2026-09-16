package com.vityarthi.academic.service;

import com.vityarthi.academic.exception.EntityNotFoundException;
import com.vityarthi.academic.model.Course;
import com.vityarthi.academic.model.Enrollment;
import com.vityarthi.academic.model.Student;
import com.vityarthi.academic.model.User;
import com.vityarthi.academic.repository.FileCourseRepository;
import com.vityarthi.academic.repository.FileEnrollmentRepository;
import com.vityarthi.academic.repository.FileUserRepository;
import com.vityarthi.academic.util.ConfigManager;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service generating academic transcripts, statistical summaries, and audit reports.
 */
public class ReportService {

    private final FileUserRepository userRepository;
    private final FileCourseRepository courseRepository;
    private final FileEnrollmentRepository enrollmentRepository;

    public ReportService(
            FileUserRepository userRepository,
            FileCourseRepository courseRepository,
            FileEnrollmentRepository enrollmentRepository
    ) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public String generateStudentTranscript(String studentId) throws EntityNotFoundException {
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        if (!(user instanceof Student student)) {
            throw new IllegalArgumentException("User " + studentId + " is not a student.");
        }

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append(String.format("                     OFFICIAL ACADEMIC TRANSCRIPT - %s\n", ConfigManager.getInstance().getInstitutionName()));
        sb.append("========================================================================================\n");
        sb.append(String.format("Student Name : %-30s Student ID : %s\n", student.getName(), student.getUserId()));
        sb.append(String.format("Major        : %-30s Semester   : %d\n", student.getMajor(), student.getSemester()));
        sb.append(String.format("Email        : %-30s Active Term: %s\n", student.getEmail(), ConfigManager.getInstance().getCurrentSemester()));
        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append(String.format("%-10s %-32s %-8s %-10s %-10s %-10s\n", "CODE", "COURSE TITLE", "CREDITS", "SCORE", "GRADE", "STATUS"));
        sb.append("----------------------------------------------------------------------------------------\n");

        int totalCredits = 0;
        int earnedCredits = 0;

        for (Enrollment e : enrollments) {
            Optional<Course> cOpt = courseRepository.findById(e.getCourseCode());
            String title = cOpt.map(Course::getCourseTitle).orElse("Unknown Course");
            int credits = cOpt.map(Course::getCredits).orElse(3);
            totalCredits += credits;

            String scoreStr = (e.getGrade() != null) ? String.format("%.1f%%", e.getGrade().getNumericalScore()) : "N/A";
            String gradeStr = (e.getGrade() != null) ? e.getGrade().getLetterGrade() : "In-Prog";
            if (e.getGrade() != null && e.getGrade().isPassing()) {
                earnedCredits += credits;
            }

            sb.append(String.format("%-10s %-32s %-8d %-10s %-10s %-10s\n",
                    e.getCourseCode(),
                    title.length() > 30 ? title.substring(0, 27) + "..." : title,
                    credits,
                    scoreStr,
                    gradeStr,
                    e.getStatus().name()));
        }

        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append(String.format("Total Attempted Credits: %d | Total Earned Credits: %d | Cumulative GPA: %.2f\n",
                totalCredits, earnedCredits, student.getCumulativeGpa()));
        sb.append(String.format("Academic Standing: %s\n", student.getCumulativeGpa() >= 8.0 ? "Dean's Honor List / Distinction" :
                (student.getCumulativeGpa() >= 6.0 ? "Good Standing" : "Academic Review Required")));
        sb.append("========================================================================================\n");

        return sb.toString();
    }

    public String generateCourseAnalytics(String courseCode) throws EntityNotFoundException {
        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));

        List<Enrollment> enrollments = enrollmentRepository.findByCourseCode(courseCode);

        DoubleSummaryStatistics stats = enrollments.stream()
                .filter(Enrollment::isCompleted)
                .mapToDouble(e -> e.getGrade().getNumericalScore())
                .summaryStatistics();

        Map<String, Integer> gradeDist = new HashMap<>();
        int passingCount = 0;

        for (Enrollment e : enrollments) {
            if (e.isCompleted()) {
                String letter = e.getGrade().getLetterGrade();
                gradeDist.put(letter, gradeDist.getOrDefault(letter, 0) + 1);
                if (e.getGrade().isPassing()) {
                    passingCount++;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append(String.format("                    COURSE ANALYTICS & GRADE AUDIT: %s\n", course.getCourseCode()));
        sb.append("========================================================================================\n");
        sb.append(String.format("Title: %s | Credits: %d | Dept: %s\n", course.getCourseTitle(), course.getCredits(), course.getDepartment()));
        sb.append(String.format("Instructor: %s | Capacity: %d | Enrolled: %d | Available Seats: %d\n",
                course.getInstructorId(), course.getCapacity(), course.getEnrolledStudentIds().size(), course.getAvailableSeats()));
        sb.append("----------------------------------------------------------------------------------------\n");

        if (stats.getCount() > 0) {
            double passRate = (passingCount * 100.0) / stats.getCount();
            sb.append(String.format("Graded Submissions : %d\n", stats.getCount()));
            sb.append(String.format("Class Average Score: %.2f%%\n", stats.getAverage()));
            sb.append(String.format("Highest Score      : %.2f%%\n", stats.getMax()));
            sb.append(String.format("Lowest Score       : %.2f%%\n", stats.getMin()));
            sb.append(String.format("Pass Rate          : %.1f%% (%d passed / %d graded)\n", passRate, passingCount, (int) stats.getCount()));
            sb.append("Grade Distribution :\n");
            gradeDist.forEach((k, v) -> sb.append(String.format("  Grade %-3s : %s (%d)\n", k, "*".repeat(v), v)));
        } else {
            sb.append("No completed evaluations recorded yet for this course.\n");
        }
        sb.append("========================================================================================\n");
        return sb.toString();
    }

    public String generateSystemSummaryReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append(String.format("             %s - SYSTEM EXECUTIVE REPORT\n", ConfigManager.getInstance().getInstitutionName()));
        sb.append(String.format("             Active Semester: %s | Grading Scheme: %s\n",
                ConfigManager.getInstance().getCurrentSemester(),
                ConfigManager.getInstance().getActiveGradingStrategy().getStrategyName()));
        sb.append("========================================================================================\n");
        sb.append(String.format("Total Registered Users    : %d\n", userRepository.count()));
        sb.append(String.format("Total Courses in Catalog  : %d\n", courseRepository.count()));
        sb.append(String.format("Total Course Registrations: %d\n", enrollmentRepository.count()));
        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append("Course Enrollment Overview:\n");
        for (Course c : courseRepository.findAll()) {
            sb.append(String.format("  • %-10s | %-30s | %2d/%2d seats filled\n",
                    c.getCourseCode(),
                    c.getCourseTitle().length() > 30 ? c.getCourseTitle().substring(0, 27) + "..." : c.getCourseTitle(),
                    c.getEnrolledStudentIds().size(),
                    c.getCapacity()));
        }
        sb.append("========================================================================================\n");
        return sb.toString();
    }
}
