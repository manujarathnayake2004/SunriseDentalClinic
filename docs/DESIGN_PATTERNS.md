# Implemented Design Patterns and Evaluation

## 1. MVC (Model–View–Controller)
**Evidence:** Spring MVC controllers, Thymeleaf views and JPA/domain models.
**Why suitable:** It separates interface concerns from clinic logic, making forms and pages easier to change without rewriting persistence code.
**Trade-off:** More classes are required than in a single-file program, but the separation improves maintainability and testing.

## 2. Repository Pattern
**Evidence:** `PatientRepository`, `AppointmentRepository`, `BillRepository`, etc.
**Why suitable:** Database operations are isolated behind clear interfaces and Spring Data generates standard CRUD queries.
**Trade-off:** Complex queries still require explicit JPQL/JDBC, so the project also uses an advanced database feature service.

## 3. Service Layer
**Evidence:** `AppointmentService`, `BillService`, `PatientService`, `ReportService`, etc.
**Why suitable:** Business rules such as clinic hours, overlap checks, billing eligibility and last-admin protection are centralized rather than duplicated in controllers.
**Trade-off:** Adds an extra layer, but it enables focused unit tests and transaction boundaries.

## 4. Strategy Pattern
**Evidence:** `BillingStrategy` and `StandardBillingStrategy` in the separate billing service.
**Why suitable:** Billing calculation behavior can be replaced or extended without changing the REST controller.
**Trade-off:** One strategy is enough today, so the abstraction is slightly more complex than direct addition; however it demonstrates extensibility and is appropriate for future discount/insurance strategies.

## 5. Three-tier architecture
1. **Presentation tier:** Controllers + Thymeleaf UI
2. **Business tier:** Service classes + validation/business rules
3. **Data tier:** Repositories/JdbcTemplate + MySQL/MariaDB

The billing service is separately deployed on port `8081`, while the main clinic application runs on `8080`. This provides the required distributed web-service architecture.

## 6. Advanced database features
The application installs/uses:
- `vw_daily_appointment_summary` reporting view
- `sp_appointments_between_dates` stored procedure used for CSV export
- `fn_bill_total` function used to verify REST billing totals
- duplicate-booking INSERT/UPDATE triggers

Application-layer validation remains the first line of defence because it can give more user-friendly messages; database triggers provide an additional integrity layer.
