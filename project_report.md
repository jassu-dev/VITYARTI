# PROJECT REPORT

---

## 1. Cover Page

```
========================================================================================
                                 VITYARTHI FLIPPED PROJECT
                      ACADEMIC EVALUATION & CAPSTONE ASSESSMENT
========================================================================================

    PROJECT TITLE:
    EduPulse: Smart Academic & Course Management System

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
In modern academic ecosystems, managing courses, registrations, prerequisite chains, and student academic evaluations is a core operational necessity. Without automated controls, universities encounter scheduling conflicts, class overcrowding, and compromised educational quality resulting from prerequisite non-compliance.

**EduPulse: Smart Academic & Course Management System** is an enterprise-grade Java application constructed around foundational and advanced principles of **Object-Oriented Programming (OOP)**. The system models an academic administrative domain featuring multi-role security (Administrators, Instructors, Students), dynamic prerequisite graph validation, concurrency-safe capacity bounds, strategy-driven grading calculations, and durable file-based persistence.

The architecture strictly adheres to standard software design patterns including **Factory Method**, **Strategy Pattern**, **Singleton Pattern**, and **Repository Pattern (DAO)**, creating a decoupled, maintainable, and extensible codebase.

---

## 3. Problem Statement
Manual or loosely coupled academic recording systems suffer from fundamental structural vulnerabilities:
1. **Prerequisite Integrity Failure**: Students routinely register for advanced subjects without successfully completing the prerequisite courses, resulting in high attrition.
2. **Seat Limit Inconsistencies**: Without synchronized transaction management, courses frequently become oversubscribed, violating physical classroom and faculty-to-student ratio norms.
3. **Inflexible Grading Paradigms**: Academic institutions require adaptability to grade students under varied frameworks (e.g. 10.0 scale vs. 4.0 scale). Hard-coded grading logic makes transitioning or supporting different scales cumbersome.
4. **Data Isolation & Traceability**: Student records, instructor evaluations, and course histories often exist across disparate files without a unified data schema or audit trail.

**EduPulse** rectifies these challenges by establishing an integrated, object-oriented engine with strict domain boundaries, automated validation checks, and role-tailored workflows.

---

## 4. Functional Requirements

The system provides 5 primary functional modules, exceeding the baseline requirement of 3:

### 4.1 Module 1: Authentication & Role-Based Access Control (RBAC)
- **User Authentication**: Login via User ID or registered Email address verified against SHA-256 cryptographic hashes.
- **Role Differentiation**: Automatic dispatch to role-specific dashboards for Administrator, Faculty Instructor, and Student.
- **Session Management**: Secure tracking of authenticated user context and state.

### 4.2 Module 2: Course Catalog & Curriculum Management
- **Course Authoring**: Administrator capability to define courses specifying unique course code, title, credit weighting (1-6), department, max capacity, and faculty assignment.
- **Prerequisite Graph Management**: Ability to attach prerequisite dependencies to any course with circular dependency prevention.
- **Faculty Course Assignment**: Dynamic linking between courses and qualified instructors.

### 4.3 Module 3: Registration & Enrollment Engine
- **Prerequisite Validation**: Dynamic verification that the student has earned passing credit in all prerequisite courses before granting registration.
- **Capacity Enforcement**: Atomic seat checks rejecting enrollments when a course section reaches max capacity.
- **Credit Limit & Duplicate Check**: Enforces maximum term registration limits (up to 6 courses) and prevents duplicate enrollments.
- **Course Withdrawal / Drop**: Enables students to drop active courses, automatically freeing seat capacity in the course catalog.

### 4.4 Module 4: Evaluation & Grading Engine
- **Grade Submission**: Instructors submit numerical percentage scores (0.0 - 100.0) for enrolled students in their assigned courses.
- **Strategy-Driven Evaluation**: Scores are dynamically evaluated into letter grades and grade points based on the active institutional strategy (`HonorsGradingStrategy` or `StandardGradingStrategy`).
- **Cumulative GPA Derivation**: Automated credit-weighted CGPA computation updated immediately upon grade recording.

### 4.5 Module 5: Analytics & Academic Reporting Engine
- **Official Transcript Generation**: Generates comprehensive transcripts detailing completed and in-progress courses, credit tallies, CGPA, and Academic Standing.
- **Course Performance Analytics**: Computes statistical summaries including class average, minimum/maximum scores, pass rate percentages, and grade distribution histograms.
- **System Executive Summary**: Institutional report showing macro-level metrics across all users, courses, and seat occupancy.

---

## 5. Non-Functional Requirements

### 5.1 Usability
- The user interface is driven by a clean, formatted terminal console featuring organized ASCII menus, structured tables, and intuitive input prompts.
- Informative, user-friendly error messages guide the user whenever operations fail.

### 5.2 Security & Data Integrity
- Passwords are never stored in plaintext; all user credentials undergo SHA-256 hashing.
- Encapsulation guarantees that internal model states (such as course rosters and completed credits) cannot be modified from outside validated domain methods.

### 5.3 Reliability & Durability
- The application implements file-backed persistence utilizing Java Object Serialization.
- Repositories automatically flush state to disk upon modification, ensuring zero data loss upon application restart.

### 5.4 Maintainability & Extensibility
- Clean separation of concerns following a layered architecture: Model $\rightarrow$ Repository $\rightarrow$ Service $\rightarrow$ UI.
- Polymorphic design ensures new user roles or alternative grading scales can be added without altering existing business logic.

---

## 6. System Architecture

EduPulse follows a robust **Four-Tier Layered Architecture**:

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
                              +-------------------------------------------+
                              |         EduPulse Academic System          |
                              +-------------------------------------------+
                                                    |
            +---------------------------------------+---------------------------------------+
            |                                       |                                       |
            v                                       v                                       v
     (( Administrator ))                     (( Instructor ))                         (( Student ))
            |                                       |                                       |
            +--> [ Login ]                          +--> [ Login ]                          +--> [ Login ]
            +--> [ Create Course ]                  +--> [ View Assigned Courses ]          +--> [ View Academic Profile ]
            +--> [ Set Prerequisites ]              +--> [ View Course Roster ]             +--> [ Browse Catalog ]
            +--> [ Assign Instructor ]              +--> [ Submit Grades ]                  +--> [ Register for Course ]
            +--> [ Register New User ]              +--> [ View Course Analytics ]          +--> [ Drop Course ]
            +--> [ Toggle Grading Scale ]           +--> [ Logout ]                         +--> [ View Official Transcript ]
            +--> [ View System Summary ]                                                    +--> [ Logout ]
            +--> [ Logout ]
```

### 7.2 Process Flow / Workflow Diagram

```
[Start Application]
        |
        v
[Initialize Repositories & Load Data Files]
        |
        v
[Is Database Empty?] --YES--> [DataInitializer seeds benchmark records]
        |                                       |
        +<--------------------------------------+
        v
[Display Guest Menu]
        |
        +---> [1. Login] ------------> [Validate SHA-256 Credentials]
        |                                       |
        |                         +-------------+-------------+
        |                         |             |             |
        |                         v             v             v
        |                     (Admin)     (Instructor)    (Student)
        |                     Console       Console        Console
        |
        +---> [2. Browse Catalog] ----> [Display Courses & Prerequisites]
        |
        +---> [3. Run Test Suite] ----> [Execute 11 Automated System Tests]
        |
        +---> [4. Exit] --------------> [Flush Data & Terminate]
```

### 7.3 Sequence Diagram (Student Course Registration with Prerequisite Check)

```
Student               ConsoleUI           EnrollmentService       CourseRepo       Student (Model)
   |                      |                       |                    |                  |
   |-- 1. Enter Course -->|                       |                    |                  |
   |                      |-- 2. enrollStudent -->|                    |                  |
   |                      |                       |-- 3. findById ---->|                  |
   |                      |                       |<-- course obj -----|                  |
   |                      |                       |                                       |
   |                      |                       |-- 4. Check isFull() ----------------->|
   |                      |                       |<-- (false) ---------------------------|
   |                      |                       |                                       |
   |                      |                       |-- 5. Check completed prerequisites ->|
   |                      |                       |   [Check missingPrereqs]              |
   |                      |                       |                                       |
   |                      |                       |-- 6. Add student to course roster --->|
   |                      |                       |-- 7. Add course to student's list --->|
   |                      |                       |-- 8. Save updated entities ---------->|
   |                      |<-- Enrollment Object -|                                       |
   |<-- Success Alert ----|                       |                                       |
```

### 7.4 Class Diagram / Component Diagram

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

## 8. Design Decisions & Rationale

1. **Layered Architecture over Monolithic Scripting**:
   - *Rationale*: Separating model entities, repositories, service orchestrators, and UI guarantees that changes in the user interface do not affect business validation rules.
2. **Generic Repository Interface (`Repository<T, ID>`)**:
   - *Rationale*: Adopting the Data Access Object (DAO) pattern decouples the persistence layer. Currently backed by serialized files, it can seamlessly swap to JDBC/Hibernate without modifying any service code.
3. **Strategy Pattern for Academic Grading**:
   - *Rationale*: Universities frequently utilize distinct grading scales (e.g. 10.0 scale vs 4.0 scale). The Strategy Pattern permits hot-swapping grading algorithms at runtime via `ConfigManager`.
4. **Custom Checked Exception Hierarchy**:
   - *Rationale*: Using specialized checked exceptions (`CourseFullException`, `PrerequisiteNotMetException`, `AuthenticationException`) ensures explicit error handling and descriptive feedback to users.
5. **Standard Java Serialization without External Libraries**:
   - *Rationale*: Minimizes external dependencies, ensuring the project builds and runs on any standard Java Development Kit installation.

---

## 9. Implementation Details

### Demonstration of Core OOP Principles
- **Encapsulation**: All fields in `User`, `Student`, `Course`, and `Enrollment` are marked `private`. Mutations occur exclusively through validated setter methods or domain actions (such as `course.addStudent()`).
- **Inheritance**: `Student`, `Instructor`, and `Administrator` extend the abstract base class `User`, inheriting core identification, authentication, and status attributes.
- **Polymorphism**: The abstract methods `getRoleSpecificDetails()` and `getMaxCourseAllowance()` are dynamically dispatched at runtime based on the underlying subclass.
- **Abstraction & Interfaces**: The `GradingStrategy` and `Repository<T, ID>` interfaces define contracts without exposing internal algorithmic or file storage mechanics.

### Design Patterns Implemented
- **Singleton Pattern**: `ConfigManager.getInstance()` ensures a globally accessible, thread-safe configuration context.
- **Factory Method Pattern**: `UserFactory.createUser(...)` encapsulates polymorphic user instantiation.
- **Strategy Pattern**: `StandardGradingStrategy` and `HonorsGradingStrategy` implement `GradingStrategy` to allow interchangeable evaluation schemes.
- **Repository Pattern**: `FileUserRepository`, `FileCourseRepository`, and `FileEnrollmentRepository` abstract data persistence.

---

## 10. Screenshots & Results

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

### 10.2 Student Academic Transcript Generation
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

### 10.3 Course Analytics & Audit Output
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

Testing adhered to a structured, multi-tier strategy:
1. **Unit Testing**: Validating isolated logic including password hashing (`SecurityUtils`), user instantiation (`UserFactory`), and score mapping (`GradingStrategy`).
2. **Integration Testing**: Testing repository save, retrieve, and auto-flush mechanisms with transient file stores.
3. **Domain Constraint & Invariant Testing**:
   - Verified that enrolling in a course with unsatisfied prerequisites throws `PrerequisiteNotMetException`.
   - Verified that enrolling in a full course throws `CourseFullException`.
   - Verified that attempting to register for the same course twice throws `ValidationException`.
4. **Calculations Verification**: Validating the weighted cumulative GPA arithmetic against hand-computed expected results.

---

## 12. Challenges Faced

1. **Circular Prerequisite Deadlocks**:
   - *Challenge*: Allowing administrators to set prerequisites risked inadvertent cycles (e.g. Course A requires Course B, which requires Course A).
   - *Solution*: Implemented graph cycle detection during prerequisite assignment in `CourseService.addPrerequisite()`.
2. **Dynamic GPA Recalculation across Variable Credits**:
   - *Challenge*: Simple arithmetic averages fail when courses have variable credit weights (e.g. 4-credit Java vs 2-credit lab).
   - *Solution*: Built a credit-weighted GPA aggregator in `EnrollmentService.recalculateGpa()` multiplying grade points by credits and dividing by total earned credits.
3. **Concurrency & File Durability**:
   - *Challenge*: Multiple enrollment actions in rapid succession could corrupt file state.
   - *Solution*: Utilized `ConcurrentHashMap` combined with `synchronized` methods for critical write and flush operations.

---

## 13. Learnings & Key Takeaways

- Practical mastery of core **OOP principles**: Encapsulation, Polymorphism, Inheritance, and Abstraction in a real-world enterprise domain.
- Effective adoption of **GoF Software Design Patterns** (Singleton, Factory Method, Strategy, DAO) to build clean, maintainable systems.
- Designing self-contained **automated test suites** in pure Java to guarantee high software reliability and regressions prevention.
- Implementing robust **exception handling architectures** distinguishing business constraint violations from fatal runtime errors.

---

## 14. Future Enhancements

1. **Graphical User Interface (GUI) / Web Portal**: Transition the terminal CLI to a modern JavaFX desktop app or Spring Boot + React web application.
2. **Relational Database Migration**: Transition file serialization to PostgreSQL / MySQL via Hibernate ORM.
3. **Automated Notification System (Observer Pattern)**: Send automated email alerts to students when course grades or prerequisite announcements are published.
4. **Timetable & Clash Detection**: Incorporate weekly schedule slot allocations (e.g. Monday 9-10 AM) and automated conflict detection.

---

## 15. References

1. Oracle Java SE Documentation: [https://docs.oracle.com/en/java/](https://docs.oracle.com/en/java/)
2. Gamma, E., Helm, R., Johnson, R., & Vlissides, J. *Design Patterns: Elements of Reusable Object-Oriented Software* (Addison-Wesley).
3. Bloch, Joshua. *Effective Java* (3rd Edition, Addison-Wesley).
4. VIT Course Syllabus for Object-Oriented Programming (Java).
