# Project Report: EduPulse Academic and Course Management System

---

## 1. Cover Page

```
========================================================================================
                                 VITYARTHI FLIPPED PROJECT
                      ACADEMIC EVALUATION & CAPSTONE ASSESSMENT
========================================================================================

    PROJECT TITLE:
    EduPulse: Academic and Course Management System

    COURSE NAME:
    Object-Oriented Programming (Java)

    COURSE CODE:
    CSE2006

    SUBMITTED BY:
    Student Name : Maddala Jashwanth
    Register No. : 25BAI10796
    Program      : B.Tech Computer Science and Engineering (AI & DS)
    Institution  : School of Computing Science and Engineering (SCSE)

    SUBMITTED TO:
    Course Faculty & Academic Evaluation Committee
    VITyarthi / VIT University

    DATE OF SUBMISSION:
    September 2026

========================================================================================
```

---

## 2. Introduction

Managing student course registrations, tracking prerequisites, ensuring class sizes stay within room capacities, and calculating semester GPAs are fundamental operational tasks in every academic institution. When these processes are handled manually or across fragmented spreadsheets, colleges encounter frequent data entry mistakes, students taking advanced courses without foundational knowledge, and errors in calculating weighted GPAs.

The purpose of this project, **EduPulse**, is to build a reliable, modular, and object-oriented academic management application in pure Java. Developed as part of the flipped learning evaluation for **CSE2006 Object-Oriented Programming**, the system implements a role-based console application for Administrators, Faculty, and Students.

The application emphasizes clean Object-Oriented Programming (OOP) design:
- **Encapsulation:** Keeping all critical fields private with strict validation on mutators.
- **Inheritance & Polymorphism:** Establishing a base `User` class extended by `Student`, `Instructor`, and `Administrator` with dynamic method dispatch.
- **Abstraction:** Generic repository interfaces (`Repository<T, ID>`) and grading interfaces (`GradingStrategy`).
- **Design Patterns:** Factory Method, Strategy Pattern, Singleton Pattern, and Data Access Object (DAO) Repository Pattern.
- **Data Persistence:** Using Java binary serialization to persist state between runs without third-party database dependencies.

---

## 3. Problem Statement

At modern academic institutions, course registration and grade tracking systems often face the following practical challenges:

1. **Prerequisite Violations:** Students often attempt to enroll in advanced courses without passing foundational prerequisites. Manual checks are prone to human oversight.
2. **Classroom Capacity Over-Subscription:** Popular course sections fill up quickly. Without automated real-time seat tracking, student enrollments can exceed available room seats.
3. **Complex Grade and GPA Calculations:** Different degree programs or institutions alternate between 10-point and 4.0 grading scales. Calculating credit-weighted cumulative GPAs by hand often causes arithmetic inconsistencies.
4. **Data Disconnection Across Departments:** Faculty mark sheets, student enrollment lists, and administrator master records are often maintained separately, causing synchronization issues.

EduPulse addresses these issues by combining authentication, course catalog management, prerequisite verification, dynamic seat reservation, and automatic GPA calculations into one cohesive Java application.

---

## 4. Functional Requirements

The system implements 5 functional modules (exceeding the required minimum of 3):

### 4.1 Module 1: Authentication and Role-Based Access Control (RBAC)
- **Secure Authentication:** Users log in using either their User ID or registered Email address. Passwords are verified against SHA-256 cryptographic hashes.
- **Role-Based Menus:** Upon successful login, users are routed to tailored dashboards:
  - Administrator Console
  - Faculty Instructor Dashboard
  - Student Portal
- **Session State:** Maintains active session information with full logout capability.

### 4.2 Module 2: Course Catalog and Curriculum Management
- **Course Creation:** Administrators can add new courses with details including Course Code, Title, Credits (1 to 6), Department, Maximum Capacity, and Assigned Faculty.
- **Prerequisite Definition:** Administrators can link prerequisite courses to an existing course, with automatic detection to prevent circular prerequisite dependencies.
- **Faculty Assignment:** Assigning qualified instructors to courses and displaying assigned courses in faculty dashboards.

### 4.3 Module 3: Student Registration and Enrollment Engine
- **Prerequisite Check:** Enforces that a student must have completed and passed all prerequisites before enrolling.
- **Capacity Enforcement:** Blocks registration if the course has reached its maximum seat capacity.
- **Credit Limit & Duplicate Check:** Restricts students to a maximum of 6 concurrent courses per term and rejects duplicate enrollments.
- **Course Drop Lifecycle:** Allows students to drop an active course, which automatically restores seat capacity for other students.

### 4.4 Module 4: Evaluation and Grading Engine
- **Score Entry:** Faculty can record numerical marks (0.0 to 100.0) for students in their assigned courses.
- **Dynamic Grading Scale (Strategy Pattern):** Evaluates percentage marks into letter grades using either the VIT 10-point scale or the standard 4.0 scale.
- **Automatic GPA Derivation:** Automatically updates the student's cumulative GPA across all completed credits upon grade submission.

### 4.5 Module 5: Transcripts and Academic Analytics
- **Official Transcript Generation:** Produces a formatted student transcript displaying all attempted courses, credits, numerical marks, letter grades, status, total earned credits, and CGPA.
- **Course Analytics:** Produces class statistics including total graded submissions, average marks, highest/lowest scores, pass rates, and an ASCII grade distribution histogram.
- **Institutional Summary:** Displays a system-wide overview of total registered users, courses, and seat occupancy rates.

---

## 5. Non-Functional Requirements

1. **Performance:** All in-memory lookups use `ConcurrentHashMap`, ensuring O(1) response times during student catalog lookups and registrations.
2. **Security:** Passwords are never stored in plaintext; SHA-256 hashing is enforced through a centralized `SecurityUtils` utility.
3. **Usability:** The console interface uses formatted tables, clean separation lines, and clear error prompts to guide users through each action.
4. **Reliability and Data Durability:** Binary object serialization (`users.dat`, `courses.dat`, `enrollments.dat`) automatically flushes to disk after modifications, ensuring zero data loss upon application exit.
5. **Maintainability and Extensibility:** Decoupled layered architecture (Model, Repository, Service, UI) allows replacing the file storage layer with a relational SQL database without modifying business logic.

---

## 6. System Architecture

The project follows a standard **Four-Tier Layered Architecture**:

```
+-------------------------------------------------------------------------+
|                         PRESENTATION LAYER                              |
|   ConsoleUI (Interactive Menu, Input Prompts, Formatted Output)        |
+-------------------------------------------------------------------------+
                                    |
                                    v
+-------------------------------------------------------------------------+
|                           SERVICE LAYER                                 |
|  - AuthenticationService   - CourseService                              |
|  - EnrollmentService       - ReportService                              |
+-------------------------------------------------------------------------+
          |                         |                         |
          v                         v                         v
+--------------------+    +--------------------+    +---------------------+
|    DESIGN PATTERNS |    |    DOMAIN MODEL    |    |  EXCEPTION SYSTEM   |
| - UserFactory      |    | - User (Abstract)  |    | - AcademicException |
| - GradingStrategy  |    | - Student          |    | - CourseFullEx      |
| - ConfigManager    |    | - Instructor       |    | - PrereqNotMetEx    |
|   (Singleton)      |    | - Admin / Course   |    | - ValidationEx      |
+--------------------+    +--------------------+    +---------------------+
                                    |
                                    v
+-------------------------------------------------------------------------+
|                        PERSISTENCE / DAO LAYER                          |
|  - Repository<T, ID> (Generic Interface)                                |
|  - FileUserRepository  - FileCourseRepository  - FileEnrollmentRepo     |
+-------------------------------------------------------------------------+
                                    |
                                    v
+-------------------------------------------------------------------------+
|                          STORAGE / DATA FILES                           |
|       data/users.dat        data/courses.dat      data/enrollments.dat  |
+-------------------------------------------------------------------------+
```

---

## 7. Design Diagrams

### 7.1 Use Case Diagram

```
                     EduPulse Academic Management System
+-------------------------------------------------------------------------+
| [Administrator]   --> (Login)                                           |
|                   --> (Create Course & Set Prerequisite Rules)          |
|                   --> (Assign Faculty to Course)                        |
|                   --> (Register New Users)                              |
|                   --> (Toggle Institutional Grading Strategy Scheme)    |
|                   --> (View System Executive Report)                    |
|                                                                         |
| [Instructor]      --> (Login)                                           |
|                   --> (View Assigned Course Sections)                   |
|                   --> (Inspect Course Roster)                           |
|                   --> (Submit Numerical Scores / Evaluate Grades)       |
|                   --> (Inspect Course Analytics & Histograms)           |
|                                                                         |
| [Student]         --> (Login)                                           |
|                   --> (View Academic Profile & Completed Credits)       |
|                   --> (Browse Catalog with Prerequisites)               |
|                   --> (Register for Eligible Courses)                   |
|                   --> (Drop In-Progress Courses)                        |
|                   --> (Generate & View Official Academic Transcript)    |
+-------------------------------------------------------------------------+
```

### 7.2 Process Flow / Workflow Diagram

```
[System Launch]
      |
      v
[Initialize Repositories & Load Data Files (.dat)]
      |
      v
[Is Fresh Database?] --YES--> [DataInitializer seeds benchmark records]
      |
      v
[Display Interactive Guest Menu]
      |
      +---> [1. User Login] ----------> [Verify SHA-256 Hash]
      |                                        |
      |                          +-------------+-------------+
      |                          |             |             |
      |                          v             v             v
      |                      (Admin)      (Instructor)    (Student)
      |                      Dashboard     Dashboard      Dashboard
      |
      +---> [2. Browse Catalog] ------> [Display Courses, Seats & Prerequisites]
      |
      +---> [3. Run Diagnostics] -----> [Execute 11 Automated System Tests]
      |
      +---> [4. Exit Application] ----> [Flush In-Memory Stores & Terminate]
```

### 7.3 Sequence Diagram: Student Course Enrollment Flow

```
Student           ConsoleUI           EnrollmentService       CourseRepo       Student (Model)
   |                  |                       |                    |                  |
   |-- Enter Code --->|                       |                    |                  |
   |                  |-- enrollStudent ----->|                    |                  |
   |                  |                       |-- findById ------->|                  |
   |                  |                       |<-- course obj -----|                  |
   |                  |                       |                                       |
   |                  |                       |-- Check course.isFull() ------------->|
   |                  |                       |<-- (false, seats available) ----------|
   |                  |                       |                                       |
   |                  |                       |-- Check completed prerequisites ----->|
   |                  |                       |   [Check missingPrereqs list]         |
   |                  |                       |                                       |
   |                  |                       |-- Add student to course roster ------>|
   |                  |                       |-- Add course to student enrolled list>|
   |                  |                       |-- Persist changes to Repository ----->|
   |                  |<-- Enrollment Success-|                                       |
   |<-- Alert Banner -|                       |                                       |
```

### 7.4 Class / Component Diagram

```
+----------------------------------------------------+
|                   <<abstract>>                     |
|                       User                         |
+----------------------------------------------------+
| - userId: String                                   |
| - name: String                                     |
| - email: String                                    |
| - passwordHash: String                             |
| - role: Role                                       |
| - active: boolean                                  |
+----------------------------------------------------+
| + getRoleSpecificDetails(): String (abstract)      |
| + getMaxCourseAllowance(): int (abstract)          |
+----------------------------------------------------+
         ^                     ^                  ^
         |                     |                  |
+------------------+  +-----------------+  +-------------------+
|     Student      |  |   Instructor    |  |   Administrator   |
+------------------+  +-----------------+  +-------------------+
| - major: String  |  | - dept: String  |  | - adminLvl: String|
| - semester: int  |  | - desg: String  |  | - office: String  |
| - cgpa: double   |  | - courses: List |  +-------------------+
| - completed: Set |  +-----------------+
+------------------+

+-------------------------------------+       +------------------------------------+
|               Course                |       |             Enrollment             |
+-------------------------------------+       +------------------------------------+
| - courseCode: String                |       | - enrollmentId: String             |
| - courseTitle: String               | 1   * | - studentId: String                |
| - credits: int                      |------>| - courseCode: String               |
| - capacity: int                     |       | - semester: String                 |
| - instructorId: String              |       | - status: Status                   |
| - prerequisiteCodes: List<String>   |       | - grade: Grade                     |
+-------------------------------------+       +------------------------------------+
```

### 7.5 Database / Storage ER Diagram

```
+---------------------------+             +---------------------------+
|          USER             |             |          COURSE           |
+---------------------------+             +---------------------------+
| PK  userId        VARCHAR |             | PK  courseCode    VARCHAR |
|     name          VARCHAR |             |     courseTitle   VARCHAR |
|     email         VARCHAR | 1         * |     credits       INT     |
|     passwordHash  VARCHAR |<------------|     department    VARCHAR |
|     role          VARCHAR | (Taught by) |     capacity      INT     |
|     major/dept    VARCHAR |             | FK  instructorId  VARCHAR |
+---------------------------+             +---------------------------+
              | 1                                       | 1
              |                                         |
              | *                                       | *
+---------------------------------------------------------------------+
|                             ENROLLMENT                              |
+---------------------------------------------------------------------+
| PK  enrollmentId    VARCHAR                                         |
| FK  studentId       VARCHAR                                         |
| FK  courseCode      VARCHAR                                         |
|     semester        VARCHAR                                         |
|     enrollmentDate  DATE                                            |
|     status          VARCHAR (ENROLLED | COMPLETED | DROPPED)        |
|     score           DOUBLE                                          |
|     letterGrade     VARCHAR (S, A, B, C, D, E, F)                   |
|     gradePoint      DOUBLE                                          |
+---------------------------------------------------------------------+
```

---

## 8. Design Decisions and Rationale

1. **Separation of Layers:** Keeping Model, Repository, Service, and UI in distinct packages prevents console input/output from getting entangled with registration rules and validations.
2. **Strategy Pattern for Grading:** Rather than writing hardcoded `if-else` blocks for grade conversions, the `GradingStrategy` interface allows switching grading schemes (Standard 4.0 vs VIT 10-point) at runtime without editing service code.
3. **Factory Method for User Creation:** Using `UserFactory` ensures that all user creation logic, role defaults, and SHA-256 password hashing are handled consistently in one place.
4. **Custom Checked Exception Hierarchy:** Custom exceptions like `CourseFullException`, `PrerequisiteNotMetException`, and `AuthenticationException` provide clear error messages to users rather than generic system crashes.
5. **Standard Java Serialization:** Storing data via `ObjectOutputStream` allows the application to be self-contained, running directly without requiring MySQL or external database setup.

---

## 9. Implementation Details

### Core OOP Principles Applied
- **Encapsulation:** Model entities (`User`, `Student`, `Course`, `Enrollment`) keep fields `private`. Modification is only possible through validated setters and domain methods such as `course.addStudent()`. Collections are returned via `Collections.unmodifiableList()` to prevent external tampering.
- **Inheritance:** `Student`, `Instructor`, and `Administrator` all inherit common fields (`userId`, `name`, `email`, `passwordHash`, `role`) from the abstract `User` class.
- **Polymorphism:** Methods such as `getRoleSpecificDetails()` and `getMaxCourseAllowance()` are defined as abstract in `User` and overridden specifically in each subclass.
- **Abstraction:** The service layer interacts with storage via the `Repository<T, ID>` interface, meaning the underlying storage mechanism can change without touching business logic.

### Design Patterns Used
- **Singleton Pattern:** `ConfigManager` maintains a single, synchronized application-wide configuration instance for the active semester, data directory, and current grading scheme.
- **Factory Method:** `UserFactory.createUser(...)` handles the creation of `Student`, `Instructor`, or `Administrator` objects based on role.
- **Strategy Pattern:** `StandardGradingStrategy` and `HonorsGradingStrategy` implement `GradingStrategy` to provide interchangeable evaluation algorithms.
- **Repository Pattern:** `FileUserRepository`, `FileCourseRepository`, and `FileEnrollmentRepository` handle file I/O operations cleanly.

---

## 10. Screenshots and Results

### 10.1 Automated Test Runner Output
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

### 10.2 Official Student Academic Transcript
```
========================================================================================
                     OFFICIAL ACADEMIC TRANSCRIPT - VIT Bhopal / VITyarthi University
========================================================================================
Student Name : Aditya Sharma                  Student ID : 23BCE0001
Major        : B.Tech Computer Science        Semester   : 3
Email        : aditya.s@vityarthi.edu         Active Term: Fall 2026-27
----------------------------------------------------------------------------------------
CODE       COURSE TITLE                     CREDITS  SCORE      GRADE      STATUS    
----------------------------------------------------------------------------------------
CSE2001    Object Oriented Programming...   4        94.5%      S          COMPLETED 
CSE2002    Data Structures & Algorithms     4        N/A        In-Prog    ENROLLED  
----------------------------------------------------------------------------------------
Total Attempted Credits: 8 | Total Earned Credits: 4 | Cumulative GPA: 10.00
Academic Standing: Dean's Honor List / Distinction
========================================================================================
```

### 10.3 Course Performance Analytics Output
```
========================================================================================
                    COURSE ANALYTICS & GRADE AUDIT: CSE2001
========================================================================================
Title: Object Oriented Programming (Java) | Credits: 4 | Dept: SCSE
Instructor: prof_smith | Capacity: 3 | Enrolled: 2 | Available Seats: 1
----------------------------------------------------------------------------------------
Graded Submissions : 1
Class Average Score: 94.50%
Highest Score      : 94.50%
Lowest Score       : 94.50%
Pass Rate          : 100.0% (1 passed / 1 graded)
Grade Distribution :
  Grade S   : * (1)
========================================================================================
```

---

## 11. Testing Approach

The project was validated through an automated test suite (`SystemValidationTest.java`) containing 11 test cases:
1. **UserFactory & Polymorphism:** Verifies correct subtype instantiation and role-specific credit capacities.
2. **Security Hashing:** Verifies that SHA-256 produces predictable hashes and rejects invalid credentials.
3. **Strategy Pattern:** Validates numerical-to-letter grade conversion on both 4.0 and 10.0 scales.
4. **Prerequisite Enforcement:** Tests that missing prerequisites correctly throw `PrerequisiteNotMetException`.
5. **Course Capacity Constraint:** Tests that registering past maximum capacity throws `CourseFullException`.
6. **Duplicate Registration Prevention:** Confirms that enrolling in an active course twice throws `ValidationException`.
7. **Course Drop Lifecycle:** Verifies that dropping a course updates enrollment status to `DROPPED` and frees course capacity.
8. **GPA Accuracy:** Confirms that multi-course weighted GPA calculations match expected values mathematically.

---

## 12. Challenges Faced

1. **Prerequisite Dependency Cycles:** When administrators add prerequisites, cyclic relationships (e.g., Course A requires Course B, which requires Course A) could lock students out indefinitely. This was resolved by adding validation in `CourseService.addPrerequisite()` to inspect existing prerequisite chains before saving.
2. **Credit-Weighted Cumulative GPA:** Different courses have different credit values (e.g., 4 credits for theory vs 2 credits for lab). A standard arithmetic mean produces inaccurate GPAs. The calculation was implemented as $\frac{\sum (\text{GradePoint} \times \text{Credits})}{\sum \text{Credits}}$ across completed courses.
3. **Scanner Input Trapping in CLI:** In Java console programs, reading integers via `scanner.nextInt()` leaves newline characters in the buffer, causing subsequent string inputs to skip. This was resolved by reading all console input uniformly via `scanner.nextLine()` and parsing numerical values explicitly.

---

## 13. Learnings and Key Takeaways

- Gained hands-on experience in building a complete, layered Java application using core OOP principles.
- Learned how to apply GoF design patterns (Factory, Strategy, Singleton, DAO) to real practical requirements rather than textbook examples.
- Understood the importance of defensive programming, unmodifiable collections, and custom exception hierarchies for maintaining domain integrity.
- Realized the value of writing dedicated unit and integration tests to verify edge cases early in development.

---

## 14. Future Enhancements

1. **Graphical User Interface:** Build a JavaFX desktop UI or a Spring Boot web frontend.
2. **Database Integration:** Connect to PostgreSQL or MySQL using JDBC or Hibernate ORM.
3. **Email Notification Service:** Implement the Observer pattern to notify students when grades or announcements are posted.
4. **Weekly Timetable Scheduling:** Add slot conflict detection to prevent overlapping class schedules.

---

## 15. References

1. Oracle Java SE Documentation: https://docs.oracle.com/en/java/
2. Gamma, E., Helm, R., Johnson, R., & Vlissides, J. *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley.
3. Bloch, Joshua. *Effective Java* (3rd Edition). Addison-Wesley.
4. VIT Academic Regulations and Course Syllabus for Object-Oriented Programming (Java - CSE2006).
