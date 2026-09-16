# EduPulse: Smart Academic & Course Management System

[![Java Version](https://img.shields.io/badge/Java-SE%2017%2B%20%2F%2021-orange.svg)](https://www.oracle.com/java/)
[![Build Status](https://img.shields.io/badge/Tests-11%2F11%20Passing-brightgreen.svg)]()
[![Course Alignment](https://img.shields.io/badge/Course-Java%20%2F%20OOP-blue.svg)]()
[![Course Code](https://img.shields.io/badge/Course%20Code-CSE2006-green.svg)]()
[![Student](https://img.shields.io/badge/Author-Maddala%20Jashwanth%20(25BAI10796)-blue.svg)]()

> **EduPulse** is a robust, modular University Academic and Course Management System developed in Java following clean Object-Oriented Design Principles and Architectural Patterns.
> 
> **Author:** Maddala Jashwanth  
> **Registration No.:** 25BAI10796  
> **Course:** Object-Oriented Programming (Java) | **Course Code:** CSE2006  
> **Program:** B.Tech Computer Science and Engineering (AI & DS)  

---

## 1. Overview
In contemporary higher education, institutions face significant challenges coordinating course enrollments, enforcing academic prerequisites, managing course capacities, computing weighted grade point averages (GPA/CGPA), and maintaining audit trails. 

**EduPulse** solves these challenges by providing a secure, role-tailored academic management engine. Built strictly adhering to Object-Oriented Programming (OOP) paradigms, the system implements:
- **Encapsulation & Abstraction** via strict domain invariants and generic persistence abstractions.
- **Inheritance & Polymorphism** through extensible user roles (`Student`, `Instructor`, `Administrator`) and interchangeable evaluation schemes.
- **Design Patterns**: Singleton, Factory Method, Strategy Pattern, and Repository (DAO) Pattern.
- **Durability**: Auto-flushing binary file serialization ensuring zero data loss without requiring external SQL dependencies.

---

## 2. Key Features

### 👤 Role-Based Access Control (RBAC)
- **Administrator**: Create courses, set prerequisites, assign instructors, manage user registrations, toggle institutional grading schemes, view system executive analytics.
- **Instructor / Faculty**: View assigned sections, view class rosters, evaluate submissions, submit numerical grades, inspect course statistical distributions.
- **Student**: View personalized academic profile, register for courses with automatic prerequisite checks, drop courses with automatic seat de-allocation, inspect real-time CGPA and official academic transcripts.

### 📚 Curriculum & Prerequisite Enforcement
- Automated prerequisite verification preventing enrollment if prerequisite courses are incomplete.
- Dynamic course capacity limits with strict concurrency safety.
- Hard credit limits preventing student overloads (max 6 courses per term).

### 📐 Pluggable Grading Engine (Strategy Pattern)
- **VIT 10-Point Honors Scale**: S (10.0), A (9.0), B (8.0), C (7.0), D (6.0), E (5.0), F (0.0).
- **Standard University 4.0 Scale**: A+ (4.0), A (3.7), B (3.0), C (2.5), D (2.0), F (0.0).
- Automatic weighted CGPA computation factoring in individual course credit weights.

### 📊 Comprehensive Analytics & Audit Reporting
- Official Academic Transcript generation with standing categorization (Dean's Honor List, Good Standing, Academic Review).
- Course performance analytics with class average, maximum/minimum scores, pass percentages, and ASCII histogram grade distributions.

---

## 3. Technologies & Architecture

- **Language**: Java SE 17+ (Tested on Java 21 & Java 26)
- **Paradigm**: Object-Oriented Programming (OOP), Domain-Driven Design (DDD)
- **Patterns**:
  - `Factory Method`: `UserFactory` encapsulates polymorphic user instantiations.
  - `Strategy Pattern`: `GradingStrategy`, `HonorsGradingStrategy`, `StandardGradingStrategy`.
  - `Singleton Pattern`: `ConfigManager` maintains unified runtime configurations.
  - `Repository Pattern`: Generic `Repository<T, ID>` with file-backed persistence.
- **Security**: SHA-256 password hashing via `SecurityUtils`.
- **Testing**: Dedicated standalone automated unit and regression test runner (`SystemValidationTest`).

---

## 4. Project Structure

```
VITYARTI_PROJECT/
├── README.md                      # Comprehensive project documentation
├── statement.md                   # Problem statement & scope document
├── project_report.md              # Detailed academic report (Markdown source)
├── Project_Report.pdf             # Formatted 15-section PDF report for portal upload
├── data/                          # Persistent data directory (.dat files)
├── bin/                           # Compiled Java bytecode
└── src/
    └── com/vityarthi/academic/
        ├── Main.java              # Application bootstrap & entry point
        ├── model/                 # Domain entities
        │   ├── Role.java          # Role enumeration
        │   ├── User.java          # Abstract user base entity
        │   ├── Student.java       # Polymorphic Student entity
        │   ├── Instructor.java    # Polymorphic Instructor entity
        │   ├── Administrator.java # Polymorphic Admin entity
        │   ├── Course.java        # Course domain entity
        │   ├── Grade.java         # Grade value object
        │   └── Enrollment.java    # Student course registration entity
        ├── factory/
        │   └── UserFactory.java   # Factory Pattern for user creation
        ├── strategy/
        │   ├── GradingStrategy.java       # Strategy interface
        │   ├── StandardGradingStrategy.java # 4.0 scale strategy
        │   └── HonorsGradingStrategy.java   # 10.0 scale strategy
        ├── repository/
        │   ├── Repository.java             # Generic Repository interface
        │   ├── FileUserRepository.java     # User file persistence
        │   ├── FileCourseRepository.java   # Course file persistence
        │   └── FileEnrollmentRepository.java # Enrollment file persistence
        ├── service/
        │   ├── AuthenticationService.java  # Session & Auth business logic
        │   ├── CourseService.java          # Curriculum & assignment logic
        │   ├── EnrollmentService.java      # Registration & grading logic
        │   └── ReportService.java          # Transcripts & analytics generator
        ├── exception/
        │   ├── AcademicException.java      # Custom root checked exception
        │   ├── AuthenticationException.java # Auth failures
        │   ├── CourseFullException.java    # Capacity constraint violations
        │   ├── PrerequisiteNotMetException.java # Prerequisite failures
        │   ├── EntityNotFoundException.java # Missing entity lookups
        │   └── ValidationException.java    # Input validation failures
        ├── util/
        │   ├── ConfigManager.java          # Singleton system configuration
        │   ├── SecurityUtils.java          # SHA-256 cryptographic hashing
        │   └── DataInitializer.java       # Benchmark sample data seeder
        ├── ui/
        │   └── ConsoleUI.java              # Interactive multi-role CLI interface
        └── test/
            └── SystemValidationTest.java   # Automated unit & integration test runner
```

---

## 5. Installation & Execution Guide

### Prerequisites
- Java Development Kit (JDK 17 or higher) installed and configured in `PATH`.
- Git installed.

### Step 1: Compile the Project
Open a terminal (PowerShell, Command Prompt, or Bash) in the project root:

```powershell
# Create bin directory and compile all source files
javac -d bin (Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName })
```
*(On Linux/macOS: `javac -d bin $(find src -name "*.java")`)*

### Step 2: Run the Application
```powershell
java -cp bin com.vityarthi.academic.Main
```

### Pre-Seeded Demo Credentials
| Role | User ID / Login | Password | Notes |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin` | `admin123` | System Registrar with full administrative privileges |
| **Faculty** | `prof_smith` | `prof123` | Dr. Alan Smith (Teaches CSE1001, CSE2001) |
| **Faculty** | `prof_jones` | `prof123` | Dr. Sarah Jones (Teaches CSE2002, CSE3001) |
| **Student** | `23BCE0001` | `stu123` | Aditya Sharma (Has completed CSE1001, CGPA: 10.0) |
| **Student** | `23BCE0003` | `stu123` | Rahul Verma (Fresh student, eligible for intro courses) |

---

## 6. Running Automated Tests

EduPulse includes a built-in automated test suite covering 11 critical test cases across all business domains:

```powershell
java -cp bin com.vityarthi.academic.Main --test
```

### Test Suite Execution Output
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

---

## 7. Interactive CLI Workflow Preview

### Official Student Transcript Sample Output
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

### Course Performance Analytics Sample Output
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
