# EduPulse: Academic and Course Management System

A Java-based course enrollment and academic administration system built for the CSE2006 Object-Oriented Programming flipped course evaluation.

**Author:** Maddala Jashwanth  
**Registration Number:** 25BAI10796  
**Course:** Object-Oriented Programming (Java)  
**Course Code:** CSE2006  
**Program:** B.Tech Computer Science and Engineering (AI & DS)  
**Institution:** School of Computing Science and Engineering (SCSE), VIT  

---

## 1. Project Overview

EduPulse is a console-based university management application designed to handle student course registrations, academic records, and grade evaluations. During semester registrations, colleges face recurring issues with students taking subjects without clearing prerequisites, classrooms exceeding seat limits, and calculating weighted GPAs across different courses.

This project was built to address those exact requirements while demonstrating core Object-Oriented Programming concepts in Java:
- Abstract classes and inheritance for user roles (Students, Instructors, Administrators).
- Dynamic polymorphism for role-based behaviors and interchangeable grading scales.
- Encapsulation to protect academic records and course rosters.
- Design patterns including Factory Method, Strategy, Singleton, and Repository (DAO).
- File persistence using standard Java serialization so data remains saved between program restarts without needing an external database setup.

---

## 2. Key Features

### Role-Based Access Control
- **Administrator:** Can add new courses, set prerequisite rules between subjects, assign instructors, register students or faculty, switch the university grading scheme, and view overall system enrollment stats.
- **Instructor / Faculty:** Can view assigned courses, inspect class rosters of enrolled students, input marks (0 to 100) for evaluations, and view class statistics like averages and grade distributions.
- **Student:** Can view personal profile and current CGPA, browse the course catalog, enroll in courses (with automatic prerequisite checks), drop courses, and generate an official academic transcript.

### Prerequisite and Seat Management
- Automatic validation ensures a student has cleared prerequisite subjects before enrolling in advanced courses.
- Course capacity limits prevent over-subscription; seats are freed automatically when a student drops a course.
- Term credit limits prevent students from taking more than 6 courses at a time.

### Flexible Grading System (Strategy Pattern)
- Supports both the VIT 10-point scale (S, A, B, C, D, E, F) and a standard 4.0 GPA scale.
- Automatically calculates weighted CGPA based on individual course credit values.

### Persistence and Test Automation
- Saves all user accounts, courses, and enrollments to `.dat` files using binary serialization.
- Built-in automated test suite covering 11 unit and integration test cases.

---

## 3. Project Structure

```
VITYARTI_PROJECT/
|-- README.md                      # Project documentation and setup guide
|-- statement.md                   # Problem statement, scope, and user analysis
|-- project_report.md              # Complete 15-section project report
|-- Project_Report.pdf             # Formatted academic report for portal submission
|-- data/                          # Data files storing serialized state
|-- bin/                           # Compiled Java bytecode
`-- src/
    `-- com/vityarthi/academic/
        |-- Main.java              # Main application entry point
        |-- model/                 # Domain entity classes
        |   |-- Role.java          # Enum for user roles
        |   |-- User.java          # Abstract base user class
        |   |-- Student.java       # Student entity
        |   |-- Instructor.java    # Faculty instructor entity
        |   |-- Administrator.java # System admin entity
        |   |-- Course.java        # Course details and roster
        |   |-- Grade.java         # Grade value object
        |   `-- Enrollment.java    # Registration record
        |-- factory/
        |   `-- UserFactory.java   # Factory method for user creation
        |-- strategy/
        |   |-- GradingStrategy.java         # Grading strategy interface
        |   |-- StandardGradingStrategy.java # 4.0 grading scale
        |   `-- HonorsGradingStrategy.java   # VIT 10-point scale
        |-- repository/
        |   |-- Repository.java              # Generic repository interface
        |   |-- FileUserRepository.java      # User data access
        |   |-- FileCourseRepository.java    # Course data access
        |   `-- FileEnrollmentRepository.java# Enrollment data access
        |-- service/
        |   |-- AuthenticationService.java   # Login and registration logic
        |   |-- CourseService.java           # Course management logic
        |   |-- EnrollmentService.java       # Enrollment and grading logic
        |   `-- ReportService.java           # Transcript and report generator
        |-- exception/
        |   |-- AcademicException.java       # Base custom checked exception
        |   |-- AuthenticationException.java # Invalid login credentials
        |   |-- CourseFullException.java     # Class capacity reached
        |   |-- PrerequisiteNotMetException.java # Missing prerequisite
        |   |-- EntityNotFoundException.java # Missing record lookup
        |   `-- ValidationException.java     # Invalid user input
        |-- util/
        |   |-- ConfigManager.java           # Singleton system configuration
        |   |-- SecurityUtils.java           # SHA-256 password hashing
        |   `-- DataInitializer.java        # Default seed records
        |-- ui/
        |   `-- ConsoleUI.java               # Interactive menu interface
        `-- test/
            `-- SystemValidationTest.java    # 11 automated unit test cases
```

---

## 4. How to Compile and Run

### Prerequisites
- JDK 17 or above installed and configured in your system `PATH`.
- Git installed.

### Step 1: Open Terminal
Open PowerShell or Command Prompt in the project folder:
```powershell
cd c:\Users\madda\Documents\VITYARTI_PROJECT
```

### Step 2: Compile
Compile all Java files into the `bin` directory:
```powershell
javac -d bin (Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName })
```

### Step 3: Run the Application
Start the interactive console system:
```powershell
java -cp bin com.vityarthi.academic.Main
```

### Step 4: Run Automated Tests
Run the built-in diagnostic test suite:
```powershell
java -cp bin com.vityarthi.academic.Main --test
```

---

## 5. Demo Login Credentials

The system comes pre-loaded with sample records for testing:

| Role | Username / ID | Password | Description |
| :--- | :--- | :--- | :--- |
| Administrator | admin | admin123 | System administrator (Registrar) |
| Faculty | prof_smith | prof123 | Dr. Alan Smith (Teaches CSE1001, CSE2001) |
| Faculty | prof_jones | prof123 | Dr. Sarah Jones (Teaches CSE2002, CSE3001) |
| Student | 23BCE0001 | stu123 | Aditya Sharma (Cleared CSE1001, has CGPA 10.0) |
| Student | 23BCE0003 | stu123 | Rahul Verma (New student, can test prerequisite checks) |

---

## 6. Test Suite Results

Running `java -cp bin com.vityarthi.academic.Main --test` runs all 11 test cases:

```
------------------------------------------------------------
  STARTING AUTOMATED UNIT & INTEGRATION TEST SUITE
------------------------------------------------------------
  [PASS] UserFactory creates polymorphic subtype correctly
  [PASS] Polymorphic method getMaxCourseAllowance() returns role-specific capacity
  [PASS] SHA-256 password hash verifies matching credential
  [PASS] SHA-256 password hash rejects incorrect credential
  [PASS] StandardGradingStrategy correctly maps 92% to A+ (4.0)
  [PASS] HonorsGradingStrategy correctly maps 92% to S (10.0)
  [PASS] Prerequisite enforcement blocks enrollment when prerequisite course missing
  [PASS] Capacity constraint prevents enrollment beyond course capacity limit
  [PASS] Duplicate course registration rejected with ValidationException
  [PASS] Course drop frees seat in Course and updates enrollment status
  [PASS] Weighted GPA calculated accurately across multiple credit courses

------------------------------------------------------------
  TEST EXECUTION SUMMARY: 11 / 11 PASSED (100.0% SUCCESS)
------------------------------------------------------------
```
