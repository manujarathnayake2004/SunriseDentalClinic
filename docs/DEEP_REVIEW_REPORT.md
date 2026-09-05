# Deep Program Review — Full-Rubric Build

## Review scope
The complete Java/Spring Boot source, Thymeleaf UI, XAMPP/MySQL configuration, billing web service, repositories, validation, security, database features, tests, UML sources and GitHub workflows were reviewed against the CIS6003 assessment brief and excellent-band rubric.

## Core brief functions verified in source
- Authorized staff username/password authentication with BCrypt passwords.
- No public customer self-registration and no demo/default credentials.
- One-time local first-administrator setup for a clean database.
- Patient registration with unique patient number, name, address and contact validation.
- Appointment registration with unique appointment number, patient, dentist, treatment, date and time.
- Appointment search includes exact/partial appointment number, patient and dentist.
- Complete appointment-detail page.
- Treatment-cost + consultation-fee billing through a separate REST web service.
- Billing allowed only after an appointment is completed; one bill per appointment.
- Printable patient receipt.
- Step-by-step help page.
- Secure logout that invalidates the session and removes the session cookie.

## Reliability and validation improvements
- Past appointments rejected.
- Clinic starts limited to 08:00-17:30; treatment must finish by 18:00.
- Appointment times use 15-minute intervals.
- Inactive dentists/treatments cannot be booked.
- Duration-aware dentist overlap detection in Java.
- Database triggers provide a second layer of overlap protection.
- Completed/cancelled appointments are terminal and cannot be edited incorrectly.
- Duplicate dentist/treatment names rejected case-insensitively.
- Last active administrator cannot be disabled/demoted.
- Report date ranges are validated.
- Invalid enum/type parameters return a friendly 400 page rather than an unhandled error.
- Database constraint conflicts return a friendly response.
- Billing-service unavailability produces a clear user message.

## Excellent-band Task B evidence
- Three-tier structure: Thymeleaf/controllers -> services/business rules -> repositories/database.
- Distributed architecture: clinic application (8080) calls separate REST billing service (8081).
- Patterns: MVC, Repository, Service Layer, Strategy, dependency injection.
- XAMPP MySQL/MariaDB proper relational database.
- Advanced database features: reporting view, stored procedure, billing function and duration-aware triggers.
- Sessions/cookies: Spring Security session authentication, HttpOnly cookie and SameSite=Lax.
- Decision-support reports: appointment metrics, completion rate, dentist workload, revenue, bills and daily summary.
- CSV appointment export.
- Upcoming 24-hour appointment alerts.
- Billing-service health indicator on the dashboard.
- Audit trail for login, logout and important create/update/status/billing operations.

## Testing evidence included
Automated unit/integration tests cover appointment creation and validation, duration overlap, clinic hours, billing rules and calculation, patient-number generation, reporting, system setup, staff administration, security authorization, application context and REST billing API validation. GitHub Actions runs tests and publishes Surefire/JaCoCo evidence.

## Static validation performed on this package
- Maven POM XML files parsed successfully.
- GitHub Actions YAML parsed successfully.
- Thymeleaf templates were parsed and controller-returned template names were checked.
- No missing referenced templates were found.
- No `.idea` or compiled `target` folders are included.
- No runtime demo credentials or seeded patient/dentist/treatment records are present.
- Source scan found no TODO/FIXME placeholders.
- Java source received syntax-pattern checks for unbalanced structures and common parse errors.

## Runtime verification still required on the student's computer
A complete Maven test/build could not be executed in the packaging environment because Maven Central was unreachable. Therefore do not claim that all automated tests passed until `mvn clean test` is run successfully on the Windows/XAMPP machine or GitHub Actions. Use screenshots of the genuine passing output in Task C.

## Evidence the student must genuinely produce
- Rendered UML screenshots and explanation.
- Actual runtime screenshots for every core workflow.
- Actual test execution screenshots, including TDD RED/GREEN/REFACTOR evidence.
- Public GitHub repository URL and genuine incremental commit history.
- Branch/pull-request/version-control evidence.
- Successful GitHub Actions workflow and release/tag evidence.
- Final report with Harvard citations and critical evaluation.
