# Sunrise Dental Clinic Appointment Management System

CIS6003 Advanced Programming project implementing the Sunrise Dental Clinic scenario as an authorized-staff, distributed Java web application.

## Assignment functions implemented
- Secure username/password authentication for authorized staff only
- One-time secure first Administrator setup; no demo/default credentials
- Admin and Receptionist roles with URL-level authorization
- Patient registration, edit and search
- Unique patient/appointment/bill identifiers
- Appointment registration with required patient, dentist, treatment, date and time data
- Appointment-number search and complete details display
- Duration-aware dentist overlap/double-booking prevention
- Appointment status management
- Separate REST billing web service on port 8081
- Treatment + consultation calculation using Strategy pattern
- Database function cross-check for billing totals where supported
- One bill per completed appointment
- Printable patient receipt
- Step-by-step Help Guide
- Exit System / Secure Logout with session invalidation
- Dashboard upcoming-appointment alerts
- Billing web-service health indicator
- Appointment, dentist workload, completion and revenue reports
- CSV report export using a stored procedure where supported
- Admin dentist/treatment/staff management
- Persistent audit log including login success/failure/logout
- User-friendly global error handling

## Technology
- Java 21
- Spring Boot 3.4.x
- Spring MVC / Thymeleaf
- Spring Security
- Spring Data JPA / JdbcTemplate
- XAMPP MySQL/MariaDB compatible database
- REST billing web service
- JUnit 5 / Mockito / MockMvc / H2 test profile
- JaCoCo coverage
- GitHub Actions CI and release workflow

## Architecture and patterns
- Three-tier architecture: Presentation -> Business -> Data
- MVC
- Repository pattern
- Service Layer
- Strategy pattern in the billing service

The clinic application runs on `8080`; the billing web service runs independently on `8081`.

## Database
The app targets:
- Host: `127.0.0.1`
- Port: `3306`
- Database: `sunrise_dental`
- Username: `root`
- Password: blank by default for standard local XAMPP

`createDatabaseIfNotExist=true` is enabled. Environment overrides are supported:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `BILLING_SERVICE_URL`

No database password is stored in the repository.

## First secure start
On an empty database:
1. Start XAMPP MySQL.
2. Start the billing service.
3. Start the clinic application.
4. Open `http://localhost:8080`.
5. The system redirects to `/setup` locally.
6. Create the first Administrator.
7. Sign in and add dentists/treatments.
8. Add other authorized staff if needed.

## Easiest Windows start
Double-click:
`START_SUNRISE_DENTAL.bat`

The launcher checks common Maven locations, starts the two Java services and opens the browser. `run-clinic.ps1` also attempts to start XAMPP MySQL when port 3306 is unavailable.

Use:
`STOP_SUNRISE_DENTAL.bat`

to stop Java services on ports 8080 and 8081.

## Tests
```bash
mvn clean test
```

GitHub Actions automatically runs the tests and uploads Surefire and JaCoCo evidence.

## Advanced database features
The application attempts to install these after JPA tables are ready on MySQL/MariaDB:
- `vw_daily_appointment_summary` view
- `sp_appointments_between_dates` stored procedure
- `fn_bill_total` function
- INSERT/UPDATE duplicate-slot triggers

The same definitions are retained in `database/02_advanced_features.sql` for manual inspection/evidence.

## Assessment evidence
Start with:
- `docs/FULL_MARKS_READINESS.md`
- `docs/FUNCTIONAL_AUDIT.md`
- `docs/ASSIGNMENT_ALIGNMENT.md`
- `docs/TEST_PLAN.md`
- `docs/uml/`
