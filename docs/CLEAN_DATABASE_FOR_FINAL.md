# Clean Database Before Final Demonstration

Use this only if you want the final demonstration database to contain no old development/test records.

1. Back up the current `sunrise_dental` database to an SQL file.
2. Confirm the backup can be opened and keep it outside the project folder.
3. In phpMyAdmin, drop the old `sunrise_dental` database.
4. Re-create `sunrise_dental` using `utf8mb4_unicode_ci`.
5. Start the application. JPA will create the required tables.
6. Open `http://localhost:8080` and complete `/setup` with your own authorized administrator credentials.
7. Add the actual dentists and treatments you want to demonstrate.
8. Add only the patient/appointment records needed for your assessed demonstration.

Do not claim test or operational data in the report unless you actually created and verified it.
