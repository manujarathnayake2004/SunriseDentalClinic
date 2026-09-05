USE sunrise_dental;

-- Removes ONLY the known records that were automatically inserted by older project versions.
-- Review this file before running it. Genuine clinic records are not intentionally targeted.

-- Remove the old demo staff accounts. This allows the new /setup page to create your own administrator
-- when there are no other staff accounts in the users table.
DELETE FROM users
WHERE username IN ('admin', 'reception')
  AND full_name IN ('System Administrator', 'Front Desk Receptionist');

-- Remove old seeded dentists only when they are not referenced by an appointment.
DELETE d FROM dentists d
WHERE d.dentist_name IN ('Dr. Nadeesha Perera', 'Dr. Kavindu Silva', 'Dr. Ayesha Fernando')
  AND NOT EXISTS (SELECT 1 FROM appointments a WHERE a.dentist_id = d.id);

-- Remove old seeded treatments only when they are not referenced by an appointment.
DELETE t FROM treatments t
WHERE t.treatment_name IN (
    'Dental Consultation', 'Dental Cleaning', 'Tooth Filling',
    'Tooth Extraction', 'Root Canal Treatment'
)
  AND NOT EXISTS (SELECT 1 FROM appointments a WHERE a.treatment_id = t.id);
