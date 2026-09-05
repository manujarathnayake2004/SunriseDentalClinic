# START HERE — Sunrise Dental Clinic

## Fastest startup
After Java 21, Maven and XAMPP are installed, use:

`START_SUNRISE_DENTAL.bat`

It starts the billing service and clinic application and opens the browser. Use `STOP_SUNRISE_DENTAL.bat` when finished. `CHECK_SYSTEM.bat` checks the required local services/ports.

## 1. Required software
- Java JDK 21
- Apache Maven 3.9+
- XAMPP MySQL/MariaDB
- Git
- IntelliJ IDEA (for development/demo)

Check in PowerShell:
```powershell
java -version
mvn -version
git --version
```

## 2. Database
Start XAMPP **MySQL**. Apache is only needed for phpMyAdmin.

Create once if required:
```sql
CREATE DATABASE IF NOT EXISTS sunrise_dental
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

Default local connection:
```text
DB_URL=jdbc:mysql://127.0.0.1:3306/sunrise_dental
DB_USERNAME=root
DB_PASSWORD=
```
If your XAMPP root account has a password, set `DB_PASSWORD` locally; never commit it.

## 3. First start
There are no demo/default users. With an empty database, open `http://localhost:8080` and create the first authorized administrator on `/setup`. Setup is local-only and becomes unavailable after the first staff account exists.

The administrator should then add real clinic configuration:
1. Dentists.
2. Treatments, treatment fees, consultation fees and duration.
3. Receptionist accounts if required.

## 4. Manual start if needed
Terminal 1:
```powershell
mvn -pl billing-service spring-boot:run
```
Expected: billing service on `http://localhost:8081`.

Terminal 2:
```powershell
mvn -pl clinic-app spring-boot:run
```
Expected: clinic app on `http://localhost:8080`.

## 5. Build and automated tests
```powershell
mvn clean test
mvn clean package
```
Keep screenshots of successful test output and JaCoCo/Surefire evidence for Task C.

## 6. Functional acceptance order
1. Create/login with an authorized staff account.
2. Add dentist and treatment setup as Admin.
3. Register a patient.
4. Register an appointment.
5. Prove invalid/past/out-of-hours/overlapping bookings are rejected.
6. Search the appointment by its unique appointment number.
7. Open complete appointment details.
8. Mark it Completed.
9. Generate the bill through the REST billing service.
10. Print the receipt.
11. Open reports and export appointment CSV.
12. Check Help.
13. Securely log out using **Exit System / Secure Logout**.

See `docs/RUNTIME_ACCEPTANCE_TEST.md` for the full evidence checklist.

## 7. Advanced database evidence
The application attempts to install the view, stored procedure, function and triggers automatically on MySQL/MariaDB after Hibernate has created/updated the tables. If the local database user lacks DDL privileges, manually execute:

`database/02_advanced_features.sql`

## 8. Git/GitHub
Use a public repository with genuine incremental commits and branches. Push the latest source, run GitHub Actions, create a version tag/release and capture genuine screenshots for Task D.
