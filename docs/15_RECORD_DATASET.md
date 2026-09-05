# 15-record assessment dataset

This optional dataset fills the data-driven pages with realistic fictional records for functional demonstration, testing and assessment screenshots.

It adds 15 new records to each main data area without deleting existing clinic information:

- Patients
- Dentists
- Treatments
- Staff Users
- Appointments
- Bills / Receipts
- Audit Log

Reports and Dashboard values are derived automatically from the inserted appointments and bills.

## Load it

1. Start XAMPP MySQL.
2. Run the clinic application once so Hibernate creates the tables.
3. Create the first ADMIN account through `http://localhost:8080/setup` if required.
4. Stop `ClinicApplication` for a moment.
5. Double-click `LOAD_15_RECORDS.bat` in the project root.
6. Start `BillingServiceApplication` and `ClinicApplication` again.
7. Open `http://localhost:8080` and press `Ctrl + Shift + R`.

The staff records inserted by the dataset are inactive and do not expose working login credentials. Your real administrator account remains unchanged.

The data is fictional and intended only for software demonstration and testing. It should not be described as real clinic or patient information in the report.
