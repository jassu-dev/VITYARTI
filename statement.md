# Problem Statement and Project Scope

**Project Name:** EduPulse: Academic and Course Management System  
**Student Name:** Maddala Jashwanth  
**Registration Number:** 25BAI10796  
**Course:** Object-Oriented Programming (Java)  
**Course Code:** CSE2006  
**Program:** B.Tech Computer Science and Engineering (AI & DS)  

---

## 1. Problem Statement

At university campuses, semester registration and grade tracking are often handled through disconnected systems or manual spreadsheets. This introduces several recurring practical issues:

1. **Students Enrolling Without Clearing Prerequisites:** Students frequently try to enroll in advanced core or elective subjects without passing the foundational course first. When checked manually, these cases get overlooked, causing academic difficulties later in the semester.
2. **Classroom Over-Capacity:** When courses are opened for registration, popular slots fill up rapidly. Without automated real-time seat checks, classes exceed classroom capacity and lab desk limits.
3. **Inconsistent Grading Calculations:** Calculating cumulative GPAs requires weighting each course by its credits. When institutions use different grading schemes (such as a 10-point scale versus a 4.0 scale), recalculating transcripts manually is prone to arithmetic mistakes.
4. **Scattered Student Records:** Course instructors, students, and administration often keep separate records of marks and enrollments, leading to confusion during grade finalization and transcript generation.

EduPulse was developed to solve these practical problems by providing a unified Java console application that automatically enforces prerequisite requirements, manages seat availability, handles role-based logins, and calculates credit-weighted GPAs accurately.

---

## 2. Project Scope

### Features Implemented:
- **Authentication and Roles:** Three distinct roles (Administrator, Instructor, Student) with passwords stored securely using SHA-256 hashing.
- **Course Catalog Management:** Creating courses, specifying credit counts (1 to 6 credits), setting maximum seat capacities, and assigning faculty members.
- **Prerequisite Validation:** Setting prerequisite rules between subjects, preventing circular dependencies, and verifying a student's completed courses before permitting enrollment.
- **Enrollment and Seat Management:** Real-time seat allocation, duplicate enrollment checks, maximum course registration limits (up to 6 per term), and course dropping with automatic seat restoration.
- **Evaluation and Grading:** Faculty can submit scores (0 to 100), and the system maps them to letter grades using either the VIT 10-point scale or standard 4.0 scale, automatically updating the student's cumulative GPA.
- **Transcripts and Analytics:** Printing official academic transcripts with academic standing classification, and course-level statistics (class average, highest/lowest marks, pass rate, and grade count).
- **Data Persistence:** Automatic saving and loading of all users, courses, and registrations to local `.dat` files via Java serialization.

### Items Outside Current Scope:
- Online fee payment gateway integration.
- Graphical user interface (GUI) or web front-end (currently focused on clean console interface and OOP design).
- Integration with external university database servers like Oracle or MySQL.

---

## 3. Target Users

| User Group | Typical Persona | How They Use the System |
| :--- | :--- | :--- |
| **Administrators** | Academic Registrar / Department Staff | Creates new courses, defines prerequisite rules, assigns instructors to courses, registers new students/faculty, switches grading scales, and views institutional reports. |
| **Instructors / Faculty** | Course Professors / Teaching Faculty | Checks class rosters of enrolled students, enters evaluation marks for each student, and views cohort score distributions and averages. |
| **Students** | Undergraduate / Postgraduate Students | Checks personal details and current CGPA, browses available courses and prerequisites, registers for eligible courses, drops courses if needed, and prints academic transcripts. |

---

## 4. High-Level Features

1. **Security & Authentication Module:** User login with password verification, role identification, and session tracking.
2. **Course & Curriculum Module:** Subject management with credit weighting, capacity tracking, and prerequisite enforcement.
3. **Enrollment & Registration Engine:** Real-time validation for seat limits, prerequisite verification, and duplicate check.
4. **Grading & Academic Engine:** Strategy-based score conversion, automated weighted GPA calculations, and grade history updates.
5. **Reporting & Analytics Engine:** Formatted transcripts, course-level statistics, and administrative summaries.
6. **Automated Test Suite:** Built-in self-test verifying all core business logic without external testing frameworks.
