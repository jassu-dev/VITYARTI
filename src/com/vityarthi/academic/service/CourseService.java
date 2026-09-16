package com.vityarthi.academic.service;

import com.vityarthi.academic.exception.EntityNotFoundException;
import com.vityarthi.academic.exception.ValidationException;
import com.vityarthi.academic.model.Course;
import com.vityarthi.academic.model.Instructor;
import com.vityarthi.academic.model.Role;
import com.vityarthi.academic.model.User;
import com.vityarthi.academic.repository.FileCourseRepository;
import com.vityarthi.academic.repository.FileUserRepository;

import java.util.List;

/**
 * Service managing course catalog, faculty assignments, and prerequisites.
 */
public class CourseService {

    private final FileCourseRepository courseRepository;
    private final FileUserRepository userRepository;

    public CourseService(FileCourseRepository courseRepository, FileUserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public Course createCourse(String code, String title, int credits, String department, int capacity, String instructorId)
            throws ValidationException, EntityNotFoundException {
        String cleanCode = code.trim().toUpperCase();
        if (courseRepository.existsById(cleanCode)) {
            throw new ValidationException("Course with code '" + cleanCode + "' already exists.");
        }

        if (instructorId != null && !instructorId.isBlank() && !"UNASSIGNED".equalsIgnoreCase(instructorId)) {
            User user = userRepository.findById(instructorId)
                    .orElseThrow(() -> new EntityNotFoundException("Instructor", instructorId));
            if (user.getRole() != Role.INSTRUCTOR) {
                throw new ValidationException("Assigned user " + instructorId + " is not a Faculty / Instructor.");
            }
            if (user instanceof Instructor instructor) {
                instructor.assignCourse(cleanCode);
                userRepository.save(instructor);
            }
        }

        Course course = new Course(cleanCode, title, credits, department, capacity, instructorId);
        return courseRepository.save(course);
    }

    public Course getCourse(String code) throws EntityNotFoundException {
        return courseRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Course", code));
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByInstructor(String instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    public void addPrerequisite(String courseCode, String prereqCode) throws EntityNotFoundException, ValidationException {
        Course target = getCourse(courseCode);
        Course prereq = getCourse(prereqCode);

        if (target.getCourseCode().equalsIgnoreCase(prereq.getCourseCode())) {
            throw new ValidationException("A course cannot be a prerequisite of itself.");
        }
        if (prereq.getPrerequisiteCodes().contains(target.getCourseCode())) {
            throw new ValidationException("Circular prerequisite dependency detected between "
                    + target.getCourseCode() + " and " + prereq.getCourseCode());
        }

        target.addPrerequisite(prereq.getCourseCode());
        courseRepository.save(target);
    }

    public void assignInstructor(String courseCode, String instructorId) throws EntityNotFoundException, ValidationException {
        Course course = getCourse(courseCode);
        User user = userRepository.findById(instructorId)
                .orElseThrow(() -> new EntityNotFoundException("Instructor", instructorId));
        if (user.getRole() != Role.INSTRUCTOR) {
            throw new ValidationException("User " + instructorId + " is not an instructor.");
        }

        course.setInstructorId(user.getUserId());
        courseRepository.save(course);

        if (user instanceof Instructor instructor) {
            instructor.assignCourse(course.getCourseCode());
            userRepository.save(instructor);
        }
    }
}
