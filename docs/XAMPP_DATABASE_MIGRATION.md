# XAMPP Database Setup / Migration

The application supports the XAMPP MySQL/MariaDB server at `127.0.0.1:3306`.

## Existing database migration
If you already have a `sunrise_dental` database on another MySQL Server, move it using SQL export/import. Do not copy raw database data folders between servers.

1. Start the old MySQL server.
2. Export the `sunrise_dental` schema to a self-contained `.sql` file.
3. Stop the old MySQL service so port `3306` is available.
4. Start XAMPP **MySQL**.
5. Start XAMPP **Apache** if you want to use phpMyAdmin.
6. Open `http://localhost/phpmyadmin`.
7. Create `sunrise_dental` with `utf8mb4_unicode_ci` if it does not exist.
8. Import the SQL backup.
9. Verify the tables and records before deleting the old database copy.

## New clean database
For a clean final database:
1. Start XAMPP MySQL.
2. Create an empty database named `sunrise_dental`.
3. Start the clinic application.
4. JPA creates the required tables.
5. The browser redirects to `/setup` because there are no staff accounts.
6. Create the first authorized administrator.
7. Add the clinic's real dentist and treatment configuration through the Admin interface.

## Connection configuration
Default local settings are in `clinic-app/src/main/resources/application.properties`:

```text
Host: 127.0.0.1
Port: 3306
Database: sunrise_dental
Username: root
Password: blank
```

If your local database account has a password, provide it using the `DB_PASSWORD` environment variable. Never commit a real database password to GitHub.

## Running the system
Either double-click:

`START_SUNRISE_DENTAL.bat`

or run the two Spring Boot modules separately:
- Billing service: `http://localhost:8081`
- Clinic application: `http://localhost:8080`

If XAMPP cannot start MySQL because port `3306` is in use, stop the older Windows MySQL service first.
