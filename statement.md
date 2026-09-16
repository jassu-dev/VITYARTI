# Project Problem Statement & Scope Document

## Project Title
**EduPulse: Smart Academic & Course Management System**

---

## 1. Problem Statement
In conventional university academic administration, course registration, prerequisite validation, grade evaluations, and academic audit calculations are frequently decoupled across disparate spreadsheets, outdated legacy portals, or manual approvals. This fragmented approach leads to several critical points of failure:

1. **Prerequisite Violations**: Students occasionally enroll in advanced elective or core subjects without fulfilling the foundational course competencies, degrading learning outcomes.
2. **Classroom & Laboratory Over-subscription**: Lack of real-time transactional synchronization results in enrollment beyond classroom physical capacities and instructor bandwidth.
3. **Rigid & Disconnected Grading Scales**: Institutions often adopt differing evaluation frameworks (such as absolute 4.0 GPA scales versus weighted 10-point scales) without modular systems capable of dynamically switching and computing cumulative GPAs.
4. **Lack of Transparent Academic Audits**: Students and advisors lack instant, verifiable academic transcripts reflecting real-time course completions, in-progress subjects, credit totals, and academic standing status.

**EduPulse** provides a unified, object-oriented software solution that coordinates authentication, automated prerequisite tracking, dynamic capacity management, strategy-based evaluation, and persistent reporting into a single cohesive, high-performance architecture.

---

## 2. Scope of the Project

### In-Scope:
- **Role-Based Authentication & Session Management**: Secure SHA-256 hashed password verification with distinct privilege sets for System Administrators, Instructors, and Students.
- **Academic Catalog & Course Management**: Defining courses with credit weightings, departmental categorization, seat limits, assigned faculty, and prerequisite graphs.
- **Transactional Enrollment Engine**: Real-time validation checking student eligibility, missing prerequisites, seat capacity, duplicate registrations, and max term credit limits.
- **Pluggable Evaluation Strategies**: Decoupled grading algorithms (Standard 4.0 scale and VIT 10-point Honors scale) with automatic weighted CGPA recalculation upon grade entry.
- **Audit Reports & Transcripts**: Instant generation of official academic transcripts, course-level statistical distributions (averages, pass rates, grade histograms), and executive administrative summaries.
- **Data Persistence**: Thread-safe binary object serialization maintaining data durability across application restarts without third-party database dependencies.

### Out-of-Scope (Future Enhancements):
- Direct payment gateway integration for tuition and lab fees.
- Real-time biometric attendance hardware integration.
- Distributed microservices architecture with cloud pub/sub messaging.

---

## 3. Target Users

| Target User Group | Primary Persona | System Touchpoints & Needs |
| :--- | :--- | :--- |
| **Academic Administrators** | University Registrars, Department Heads | Needs macro-level visibility into institutional enrollment counts, course creation, prerequisite graph assignment, instructor allocations, and grading scheme toggles. |
| **Faculty & Instructors** | Course Coordinators, Professors, TAs | Needs tools to view real-time class rosters, inspect student prerequisites, input numerical evaluation scores, and analyze cohort grade distribution histograms. |
| **Students** | Undergraduate & Postgraduate Learners | Needs intuitive interfaces to browse catalog offerings, register/drop courses safely, view academic progress, and download official academic transcripts. |

---

## 4. High-Level Features

1. **Role-Based Security Layer**:
   - Encrypted credential validation.
   - Dynamic polymorphic user representation (`User`, `Student`, `Instructor`, `Administrator`).
2. **Curriculum Engine**:
   - Course creation with invariant validation.
   - Directed prerequisite dependency management preventing circular prerequisites.
3. **Smart Enrollment Orchestrator**:
   - Concurrency-safe atomic enrollment and drop operations.
   - Real-time seat reservation and capacity enforcement.
4. **Strategy-Driven Grading Core**:
   - Interchangeable grading strategies (`GradingStrategy` interface) permitting run-time switching between 10.0 and 4.0 scales.
   - Automatic credit-weighted cumulative GPA derivation.
5. **Academic Intelligence & Reporting**:
   - Formatted Official Transcripts with Dean's Honor Roll distinction logic.
   - Course-level analytics with mean, max, min, and pass-rate calculations.
6. **Built-in Quality Assurance**:
   - Self-diagnosing automated regression test suite validating domain integrity.
