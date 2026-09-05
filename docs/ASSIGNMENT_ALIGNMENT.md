# CIS6003 Assignment Alignment — Sunrise Dental Clinic

## Task A — UML design (20 marks)
`docs/uml/` contains a use case diagram, detailed class diagram and four sequence diagrams. The UML reflects the Java implementation, role model, distributed billing flow, advanced reporting and business-rule validation. `ASSUMPTIONS.md` documents decisions not explicitly supplied in the scenario.

## Task B — Interactive distributed Java system (40 marks)
The final implementation includes:
- Java 21 / Spring Boot
- responsive menu-driven Thymeleaf UI
- authorized-staff-only authentication
- Admin and Receptionist roles
- BCrypt password storage and secure first-run admin setup
- patient registration/search/edit
- appointment create/search/details/edit/status
- robust unique identifiers
- duration-aware overlap prevention
- separate REST billing web service on port 8081
- Strategy pattern billing calculation
- completed-only billing and one-bill-per-appointment protection
- printable receipt
- dashboard reminder alerts and billing-service health monitoring
- management reports, completion rate and CSV export
- three-tier architecture
- MVC, Repository, Service Layer and Strategy patterns
- XAMPP MySQL/MariaDB database
- automatically installed/used DB view, stored procedure, function and triggers
- authenticated cookie/session management and secure logout
- admin master-data management and audit log
- global user-friendly error handling

## Task C — TDD/testing (20 marks)
The source includes unit tests, REST endpoint tests, Spring context integration testing, authorization integration testing, H2 test configuration, Maven automation, GitHub Actions and JaCoCo reports. `TEST_PLAN.md` gives traceability and manual cases. The student must still capture genuine RED/GREEN/REFACTOR and passing-test screenshots.

## Task D — Git/GitHub (20 marks)
Included repository support:
- `.gitignore`
- CI workflow for branches/pull requests
- automated test and coverage artifacts
- package artifacts
- tag-based release workflow producing runnable JAR releases

The student must provide genuine public repository history, branches, pull requests, commits, tags, workflow runs and release/deployment screenshots. These cannot be truthfully generated from source files alone.
