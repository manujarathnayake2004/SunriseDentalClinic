USE sunrise_dental;

-- Sunrise Dental Clinic assessment/presentation dataset
-- Adds 15 realistic fictional records to the main data sections.
-- Run only after the application has created its tables and the first ADMIN account exists.
-- This script does not delete existing clinic data.

SET @patient_base := COALESCE((
    SELECT MAX(CAST(SUBSTRING(patient_number, 5) AS UNSIGNED))
    FROM patients
    WHERE patient_number REGEXP '^PAT-[0-9]+$'
), 0);

SET @appointment_base := COALESCE((
    SELECT MAX(CAST(SUBSTRING(appointment_number, 5) AS UNSIGNED))
    FROM appointments
    WHERE appointment_number REGEXP '^APP-[0-9]+$'
), 0);

SET @bill_base := COALESCE((
    SELECT MAX(CAST(SUBSTRING(bill_number, 6) AS UNSIGNED))
    FROM bills
    WHERE bill_number REGEXP '^BILL-[0-9]+$'
), 0);

SET @p1  := CONCAT('PAT-', LPAD(@patient_base + 1, 5, '0'));
SET @p2  := CONCAT('PAT-', LPAD(@patient_base + 2, 5, '0'));
SET @p3  := CONCAT('PAT-', LPAD(@patient_base + 3, 5, '0'));
SET @p4  := CONCAT('PAT-', LPAD(@patient_base + 4, 5, '0'));
SET @p5  := CONCAT('PAT-', LPAD(@patient_base + 5, 5, '0'));
SET @p6  := CONCAT('PAT-', LPAD(@patient_base + 6, 5, '0'));
SET @p7  := CONCAT('PAT-', LPAD(@patient_base + 7, 5, '0'));
SET @p8  := CONCAT('PAT-', LPAD(@patient_base + 8, 5, '0'));
SET @p9  := CONCAT('PAT-', LPAD(@patient_base + 9, 5, '0'));
SET @p10 := CONCAT('PAT-', LPAD(@patient_base + 10, 5, '0'));
SET @p11 := CONCAT('PAT-', LPAD(@patient_base + 11, 5, '0'));
SET @p12 := CONCAT('PAT-', LPAD(@patient_base + 12, 5, '0'));
SET @p13 := CONCAT('PAT-', LPAD(@patient_base + 13, 5, '0'));
SET @p14 := CONCAT('PAT-', LPAD(@patient_base + 14, 5, '0'));
SET @p15 := CONCAT('PAT-', LPAD(@patient_base + 15, 5, '0'));

INSERT INTO patients (patient_number, patient_name, address, contact_number, created_at) VALUES
(@p1,  'Amaya Perera',       '12 Flower Road, Colombo 07',        '0772345101', DATE_SUB(NOW(), INTERVAL 15 DAY)),
(@p2,  'Dineth Fernando',    '44 Galle Road, Dehiwala',          '0718452202', DATE_SUB(NOW(), INTERVAL 14 DAY)),
(@p3,  'Sachini Silva',      '18 Station Road, Mount Lavinia',   '0756233403', DATE_SUB(NOW(), INTERVAL 13 DAY)),
(@p4,  'Kavishka Jayasinghe','27 Temple Road, Nugegoda',         '0764127804', DATE_SUB(NOW(), INTERVAL 12 DAY)),
(@p5,  'Ishara Gunasekara',  '63 High Level Road, Maharagama',   '0709981205', DATE_SUB(NOW(), INTERVAL 11 DAY)),
(@p6,  'Tharushi Rodrigo',   '31 Park Street, Colombo 02',       '0723567806', DATE_SUB(NOW(), INTERVAL 10 DAY)),
(@p7,  'Malith Senanayake',  '15 Lake Drive, Rajagiriya',        '0785224607', DATE_SUB(NOW(), INTERVAL 9 DAY)),
(@p8,  'Nethmi Wijesinghe',  '86 Main Street, Battaramulla',     '0742318908', DATE_SUB(NOW(), INTERVAL 8 DAY)),
(@p9,  'Pasindu Bandara',    '22 School Lane, Kotte',            '0776154309', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(@p10, 'Rashmi Karunaratne', '54 Hospital Road, Kalubowila',     '0713302210', DATE_SUB(NOW(), INTERVAL 6 DAY)),
(@p11, 'Chamod Ekanayake',   '11 Green Path, Colombo 03',        '0754819911', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(@p12, 'Imesha Liyanage',    '73 Dutugemunu Street, Kohuwala',   '0767704512', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(@p13, 'Shenal Wickramasinghe','29 Kandy Road, Peliyagoda',      '0702146813', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(@p14, 'Hasini Samarasinghe','41 Ward Place, Colombo 07',        '0728823414', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(@p15, 'Dulanjana Madushanka','90 Marine Drive, Colombo 06',     '0781197215', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 15 dentists
INSERT INTO dentists (dentist_name, specialization, active) VALUES
('Dr. Amandi Weerasinghe', 'General Dentistry', 1),
('Dr. Ruvin Dias', 'Orthodontics', 1),
('Dr. Thejani Peiris', 'Endodontics', 1),
('Dr. Sahan Abeysekara', 'Oral Surgery', 1),
('Dr. Minoli Pathirana', 'Periodontics', 1),
('Dr. Akila Ranasinghe', 'Prosthodontics', 1),
('Dr. Hiruni Alwis', 'Paediatric Dentistry', 1),
('Dr. Lahiru Mendis', 'Cosmetic Dentistry', 1),
('Dr. Naduni Hettiarachchi', 'Restorative Dentistry', 1),
('Dr. Vihanga Kulatunga', 'General Dentistry', 1),
('Dr. Senuri Dissanayake', 'Orthodontics', 1),
('Dr. Janith Peris', 'Endodontics', 1),
('Dr. Oshadi Herath', 'Periodontics', 1),
('Dr. Ravindu Samarawickrama', 'Oral Surgery', 1),
('Dr. Thinuli Amarasinghe', 'General Dentistry', 1);

-- 15 treatments
INSERT INTO treatments (treatment_name, treatment_cost, consultation_fee, duration_minutes, active) VALUES
('Comprehensive Dental Examination', 2500.00, 1500.00, 30, 1),
('Ultrasonic Scaling and Polishing', 5500.00, 1500.00, 45, 1),
('Composite Tooth Filling', 6500.00, 1500.00, 45, 1),
('Simple Tooth Extraction', 7000.00, 1500.00, 45, 1),
('Root Canal Therapy', 22000.00, 2000.00, 90, 1),
('Professional Teeth Whitening', 18000.00, 1500.00, 60, 1),
('Digital Dental X-Ray', 3500.00, 1000.00, 20, 1),
('Porcelain Crown', 28000.00, 2000.00, 60, 1),
('Dental Bridge Consultation', 12000.00, 1800.00, 45, 1),
('Full Denture Assessment', 15000.00, 1800.00, 60, 1),
('Orthodontic Consultation', 4500.00, 2000.00, 30, 1),
('Braces Adjustment', 6000.00, 1500.00, 30, 1),
('Gum Disease Treatment', 9000.00, 1800.00, 60, 1),
('Paediatric Dental Check-up', 3000.00, 1200.00, 30, 1),
('Emergency Dental Care', 8500.00, 2000.00, 45, 1);

-- 15 additional staff records for the Staff Users page.
-- They are intentionally INACTIVE and have no published login password.
INSERT IGNORE INTO users (full_name, username, password, role, active) VALUES
('Anjali Perera',       'frontdesk01', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Nipun Fernando',      'frontdesk02', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Shanika Silva',       'frontdesk03', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Kusal Jayawardena',   'frontdesk04', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Thilini Gunawardena', 'frontdesk05', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Ravindu Senanayake',  'frontdesk06', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Piumi Rodrigo',       'frontdesk07', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Kasun Wijesinghe',    'frontdesk08', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Nadee Bandara',       'frontdesk09', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Shehan Karunaratne',  'frontdesk10', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Dinithi Ekanayake',   'frontdesk11', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Tharindu Liyanage',   'frontdesk12', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Sachini Wickrama',    'frontdesk13', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Madusha Samarasinghe','frontdesk14', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0),
('Isuru Maduranga',     'frontdesk15', '$2a$10$ymCjfefdYfHZCufRUn6gVOPlAA3f.GHSkE31jZlnocEaMxMC86R0W', 'RECEPTIONIST', 0);

-- Appointment numbers keep the application's normal numbering sequence.
SET @a1  := CONCAT('APP-', LPAD(@appointment_base + 1, 6, '0'));
SET @a2  := CONCAT('APP-', LPAD(@appointment_base + 2, 6, '0'));
SET @a3  := CONCAT('APP-', LPAD(@appointment_base + 3, 6, '0'));
SET @a4  := CONCAT('APP-', LPAD(@appointment_base + 4, 6, '0'));
SET @a5  := CONCAT('APP-', LPAD(@appointment_base + 5, 6, '0'));
SET @a6  := CONCAT('APP-', LPAD(@appointment_base + 6, 6, '0'));
SET @a7  := CONCAT('APP-', LPAD(@appointment_base + 7, 6, '0'));
SET @a8  := CONCAT('APP-', LPAD(@appointment_base + 8, 6, '0'));
SET @a9  := CONCAT('APP-', LPAD(@appointment_base + 9, 6, '0'));
SET @a10 := CONCAT('APP-', LPAD(@appointment_base + 10, 6, '0'));
SET @a11 := CONCAT('APP-', LPAD(@appointment_base + 11, 6, '0'));
SET @a12 := CONCAT('APP-', LPAD(@appointment_base + 12, 6, '0'));
SET @a13 := CONCAT('APP-', LPAD(@appointment_base + 13, 6, '0'));
SET @a14 := CONCAT('APP-', LPAD(@appointment_base + 14, 6, '0'));
SET @a15 := CONCAT('APP-', LPAD(@appointment_base + 15, 6, '0'));

SET @previous_date := IF(DAY(CURDATE()) = 1, CURDATE(), DATE_SUB(CURDATE(), INTERVAL 1 DAY));

INSERT INTO appointments (appointment_number, patient_id, dentist_id, treatment_id, appointment_date, appointment_time, status, notes, created_at) VALUES
(@a1,  (SELECT id FROM patients WHERE patient_number=@p1  LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Amandi Weerasinghe' ORDER BY id DESC LIMIT 1), (SELECT id FROM treatments WHERE treatment_name='Comprehensive Dental Examination' ORDER BY id DESC LIMIT 1), CURDATE(),       '08:00:00', 'COMPLETED', 'Routine dental examination and oral health review.', DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
(@a2,  (SELECT id FROM patients WHERE patient_number=@p2  LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Ruvin Dias' ORDER BY id DESC LIMIT 1),             (SELECT id FROM treatments WHERE treatment_name='Orthodontic Consultation' ORDER BY id DESC LIMIT 1),            CURDATE(),       '08:15:00', 'COMPLETED', 'Orthodontic assessment and treatment planning.',       DATE_SUB(NOW(), INTERVAL 14 MINUTE)),
(@a3,  (SELECT id FROM patients WHERE patient_number=@p3  LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Thejani Peiris' ORDER BY id DESC LIMIT 1),          (SELECT id FROM treatments WHERE treatment_name='Root Canal Therapy' ORDER BY id DESC LIMIT 1),                   CURDATE(),       '08:30:00', 'COMPLETED', 'Root canal treatment follow-up.',                       DATE_SUB(NOW(), INTERVAL 13 MINUTE)),
(@a4,  (SELECT id FROM patients WHERE patient_number=@p4  LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Sahan Abeysekara' ORDER BY id DESC LIMIT 1),         (SELECT id FROM treatments WHERE treatment_name='Simple Tooth Extraction' ORDER BY id DESC LIMIT 1),              CURDATE(),       '08:45:00', 'COMPLETED', 'Extraction of damaged tooth.',                          DATE_SUB(NOW(), INTERVAL 12 MINUTE)),
(@a5,  (SELECT id FROM patients WHERE patient_number=@p5  LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Minoli Pathirana' ORDER BY id DESC LIMIT 1),          (SELECT id FROM treatments WHERE treatment_name='Gum Disease Treatment' ORDER BY id DESC LIMIT 1),                CURDATE(),       '09:00:00', 'COMPLETED', 'Periodontal care and gum assessment.',                   DATE_SUB(NOW(), INTERVAL 11 MINUTE)),
(@a6,  (SELECT id FROM patients WHERE patient_number=@p6  LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Akila Ranasinghe' ORDER BY id DESC LIMIT 1),          (SELECT id FROM treatments WHERE treatment_name='Porcelain Crown' ORDER BY id DESC LIMIT 1),                      @previous_date,  '09:15:00', 'COMPLETED', 'Crown preparation and fitting.',                            DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(@a7,  (SELECT id FROM patients WHERE patient_number=@p7  LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Hiruni Alwis' ORDER BY id DESC LIMIT 1),             (SELECT id FROM treatments WHERE treatment_name='Paediatric Dental Check-up' ORDER BY id DESC LIMIT 1),            @previous_date,  '09:30:00', 'COMPLETED', 'Routine child dental check-up.',                          DATE_SUB(NOW(), INTERVAL 9 MINUTE)),
(@a8,  (SELECT id FROM patients WHERE patient_number=@p8  LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Lahiru Mendis' ORDER BY id DESC LIMIT 1),            (SELECT id FROM treatments WHERE treatment_name='Professional Teeth Whitening' ORDER BY id DESC LIMIT 1),          @previous_date,  '10:00:00', 'COMPLETED', 'Cosmetic whitening treatment.',                           DATE_SUB(NOW(), INTERVAL 8 MINUTE)),
(@a9,  (SELECT id FROM patients WHERE patient_number=@p9  LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Naduni Hettiarachchi' ORDER BY id DESC LIMIT 1),      (SELECT id FROM treatments WHERE treatment_name='Composite Tooth Filling' ORDER BY id DESC LIMIT 1),               @previous_date,  '10:30:00', 'COMPLETED', 'Composite restoration for dental cavity.',                DATE_SUB(NOW(), INTERVAL 7 MINUTE)),
(@a10, (SELECT id FROM patients WHERE patient_number=@p10 LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Vihanga Kulatunga' ORDER BY id DESC LIMIT 1),          (SELECT id FROM treatments WHERE treatment_name='Ultrasonic Scaling and Polishing' ORDER BY id DESC LIMIT 1),       @previous_date,  '11:00:00', 'COMPLETED', 'Scaling and polishing appointment.',                      DATE_SUB(NOW(), INTERVAL 6 MINUTE)),
(@a11, (SELECT id FROM patients WHERE patient_number=@p11 LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Senuri Dissanayake' ORDER BY id DESC LIMIT 1),         (SELECT id FROM treatments WHERE treatment_name='Braces Adjustment' ORDER BY id DESC LIMIT 1),                    @previous_date,  '11:30:00', 'COMPLETED', 'Routine braces adjustment.',                              DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
(@a12, (SELECT id FROM patients WHERE patient_number=@p12 LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Janith Peris' ORDER BY id DESC LIMIT 1),             (SELECT id FROM treatments WHERE treatment_name='Digital Dental X-Ray' ORDER BY id DESC LIMIT 1),                  @previous_date,  '12:00:00', 'COMPLETED', 'Diagnostic dental radiograph.',                            DATE_SUB(NOW(), INTERVAL 4 MINUTE)),
(@a13, (SELECT id FROM patients WHERE patient_number=@p13 LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Oshadi Herath' ORDER BY id DESC LIMIT 1),             (SELECT id FROM treatments WHERE treatment_name='Full Denture Assessment' ORDER BY id DESC LIMIT 1),               @previous_date,  '13:00:00', 'COMPLETED', 'Denture assessment and measurements.',                   DATE_SUB(NOW(), INTERVAL 3 MINUTE)),
(@a14, (SELECT id FROM patients WHERE patient_number=@p14 LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Ravindu Samarawickrama' ORDER BY id DESC LIMIT 1),   (SELECT id FROM treatments WHERE treatment_name='Emergency Dental Care' ORDER BY id DESC LIMIT 1),                 @previous_date,  '14:00:00', 'COMPLETED', 'Urgent dental pain management.',                          DATE_SUB(NOW(), INTERVAL 2 MINUTE)),
(@a15, (SELECT id FROM patients WHERE patient_number=@p15 LIMIT 1), (SELECT id FROM dentists WHERE dentist_name='Dr. Thinuli Amarasinghe' ORDER BY id DESC LIMIT 1),       (SELECT id FROM treatments WHERE treatment_name='Dental Bridge Consultation' ORDER BY id DESC LIMIT 1),            @previous_date,  '15:00:00', 'COMPLETED', 'Bridge consultation and treatment discussion.',          DATE_SUB(NOW(), INTERVAL 1 MINUTE));

-- 15 generated bills linked to the 15 completed appointments.
SET @b1  := CONCAT('BILL-', LPAD(@bill_base + 1, 6, '0'));
SET @b2  := CONCAT('BILL-', LPAD(@bill_base + 2, 6, '0'));
SET @b3  := CONCAT('BILL-', LPAD(@bill_base + 3, 6, '0'));
SET @b4  := CONCAT('BILL-', LPAD(@bill_base + 4, 6, '0'));
SET @b5  := CONCAT('BILL-', LPAD(@bill_base + 5, 6, '0'));
SET @b6  := CONCAT('BILL-', LPAD(@bill_base + 6, 6, '0'));
SET @b7  := CONCAT('BILL-', LPAD(@bill_base + 7, 6, '0'));
SET @b8  := CONCAT('BILL-', LPAD(@bill_base + 8, 6, '0'));
SET @b9  := CONCAT('BILL-', LPAD(@bill_base + 9, 6, '0'));
SET @b10 := CONCAT('BILL-', LPAD(@bill_base + 10, 6, '0'));
SET @b11 := CONCAT('BILL-', LPAD(@bill_base + 11, 6, '0'));
SET @b12 := CONCAT('BILL-', LPAD(@bill_base + 12, 6, '0'));
SET @b13 := CONCAT('BILL-', LPAD(@bill_base + 13, 6, '0'));
SET @b14 := CONCAT('BILL-', LPAD(@bill_base + 14, 6, '0'));
SET @b15 := CONCAT('BILL-', LPAD(@bill_base + 15, 6, '0'));

INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b1, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 15 MINUTE) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a1;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b2, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 14 MINUTE) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a2;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b3, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 13 MINUTE) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a3;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b4, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 12 MINUTE) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a4;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b5, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 11 MINUTE) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a5;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b6, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 1 DAY) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a6;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b7, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 1 DAY) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a7;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b8, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 1 DAY) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a8;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b9, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 1 DAY) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a9;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b10, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 1 DAY) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a10;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b11, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 1 DAY) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a11;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b12, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 1 DAY) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a12;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b13, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 1 DAY) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a13;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b14, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 1 DAY) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a14;
INSERT INTO bills (bill_number, appointment_id, treatment_cost, consultation_fee, total_amount, created_at)
SELECT @b15, a.id, t.treatment_cost, t.consultation_fee, t.treatment_cost + t.consultation_fee, DATE_SUB(NOW(), INTERVAL 1 DAY) FROM appointments a JOIN treatments t ON t.id=a.treatment_id WHERE a.appointment_number=@a15;

-- 15 audit records to populate the Audit Log page.
INSERT INTO audit_log (username, action_type, description, created_at) VALUES
('system', 'PATIENT_CREATE', 'Assessment dataset patient records prepared for functional demonstration.', DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
('system', 'DENTIST_CREATE', 'Dentist records prepared for appointment scheduling demonstration.', DATE_SUB(NOW(), INTERVAL 14 MINUTE)),
('system', 'TREATMENT_CREATE', 'Treatment and pricing records prepared for billing demonstration.', DATE_SUB(NOW(), INTERVAL 13 MINUTE)),
('system', 'APPOINTMENT_CREATE', CONCAT(@a1, ' created for functional demonstration.'), DATE_SUB(NOW(), INTERVAL 12 MINUTE)),
('system', 'APPOINTMENT_CREATE', CONCAT(@a2, ' created for functional demonstration.'), DATE_SUB(NOW(), INTERVAL 11 MINUTE)),
('system', 'APPOINTMENT_CREATE', CONCAT(@a3, ' created for functional demonstration.'), DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
('system', 'APPOINTMENT_STATUS', CONCAT(@a4, ' marked COMPLETED.'), DATE_SUB(NOW(), INTERVAL 9 MINUTE)),
('system', 'APPOINTMENT_STATUS', CONCAT(@a5, ' marked COMPLETED.'), DATE_SUB(NOW(), INTERVAL 8 MINUTE)),
('system', 'BILL_CREATE', CONCAT(@b1, ' generated for ', @a1, '.'), DATE_SUB(NOW(), INTERVAL 7 MINUTE)),
('system', 'BILL_CREATE', CONCAT(@b2, ' generated for ', @a2, '.'), DATE_SUB(NOW(), INTERVAL 6 MINUTE)),
('system', 'BILL_CREATE', CONCAT(@b3, ' generated for ', @a3, '.'), DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
('system', 'REPORT_VIEW', 'Appointment and revenue report dataset is available.', DATE_SUB(NOW(), INTERVAL 4 MINUTE)),
('system', 'SECURITY_CHECK', 'Staff records remain controlled by role-based authorization.', DATE_SUB(NOW(), INTERVAL 3 MINUTE)),
('system', 'DATABASE_CHECK', 'Assessment dataset successfully stored in MySQL/MariaDB.', DATE_SUB(NOW(), INTERVAL 2 MINUTE)),
('system', 'ASSESSMENT_DATASET', '15-record assessment dataset loaded successfully.', DATE_SUB(NOW(), INTERVAL 1 MINUTE));
