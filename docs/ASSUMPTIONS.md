# Documented System Assumptions

1. **Authorized staff only** — The scenario explicitly limits access to authorized staff. The implementation therefore uses `ADMIN` and `RECEPTIONIST` roles and has no public customer self-registration.
2. **Initial administrator provisioning** — A new empty database requires a one-time local administrator setup. No default username/password is shipped with the software.
3. **Clinic operating hours** — Appointment starts are accepted from 08:00 to 17:30 in 15-minute intervals. The assumed clinic closing time is 18:00, so a treatment duration must finish by 18:00.
4. **Dentist availability** — Appointment duration is considered when checking dentist availability. Overlapping active appointments are rejected, not only appointments with the same start time. Cancelled appointments do not occupy a slot.
5. **Treatment pricing** — Treatment cost, consultation fee and expected duration are maintained by administrators in the database instead of being hard-coded.
6. **Billing point** — A bill is generated only after an appointment is marked `COMPLETED`, and each appointment can have only one bill.
7. **Billing verification** — The billing total is calculated by the separate REST service. When the advanced MySQL/MariaDB function is available, the main application cross-checks the returned total before saving the bill.
8. **Appointment history** — Completed and cancelled appointments are retained for audit and reporting rather than deleted.
9. **Unique identifiers** — Patient, appointment and bill numbers are generated from the highest existing fixed-width identifier and protected by unique database constraints.
10. **Local database** — The intended lab/development database is XAMPP MySQL/MariaDB on `127.0.0.1:3306` using database `sunrise_dental`.
11. **Advanced database features** — A reporting view, stored procedure, billing function and duplicate-slot triggers are installed automatically where the database account permits it; the SQL script is also supplied for manual evidence/setup.
12. **Receipt printing** — Printing uses the browser print function to produce a paper/PDF receipt from the dedicated receipt view.
