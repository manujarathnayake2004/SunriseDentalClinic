USE sunrise_dental;

-- Advanced features used by the application and useful as Task B database evidence.
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(60),
    action_type VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE VIEW vw_daily_appointment_summary AS
SELECT
    appointment_date,
    COUNT(*) AS total_appointments,
    SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_appointments,
    SUM(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled_appointments
FROM appointments
GROUP BY appointment_date;

DELIMITER $$

DROP PROCEDURE IF EXISTS sp_appointments_between_dates$$
CREATE PROCEDURE sp_appointments_between_dates(IN p_from DATE, IN p_to DATE)
BEGIN
    SELECT
        a.appointment_number,
        p.patient_name,
        d.dentist_name,
        t.treatment_name,
        a.appointment_date,
        a.appointment_time,
        a.status
    FROM appointments a
    INNER JOIN patients p ON p.id = a.patient_id
    INNER JOIN dentists d ON d.id = a.dentist_id
    INNER JOIN treatments t ON t.id = a.treatment_id
    WHERE a.appointment_date BETWEEN p_from AND p_to
    ORDER BY a.appointment_date, a.appointment_time;
END$$

DROP FUNCTION IF EXISTS fn_bill_total$$
CREATE FUNCTION fn_bill_total(p_treatment DECIMAL(12,2), p_consultation DECIMAL(12,2))
RETURNS DECIMAL(12,2)
DETERMINISTIC
RETURN COALESCE(p_treatment,0) + COALESCE(p_consultation,0)$$

DROP TRIGGER IF EXISTS trg_prevent_double_booking_insert$$
CREATE TRIGGER trg_prevent_double_booking_insert
BEFORE INSERT ON appointments
FOR EACH ROW
BEGIN
    DECLARE new_duration INT DEFAULT 30;
    SELECT duration_minutes INTO new_duration FROM treatments WHERE id = NEW.treatment_id;
    IF NEW.status <> 'CANCELLED' AND EXISTS (
        SELECT 1
        FROM appointments a
        INNER JOIN treatments t ON t.id = a.treatment_id
        WHERE a.dentist_id = NEW.dentist_id
          AND a.appointment_date = NEW.appointment_date
          AND a.status <> 'CANCELLED'
          AND NEW.appointment_time < ADDTIME(a.appointment_time, SEC_TO_TIME(t.duration_minutes * 60))
          AND ADDTIME(NEW.appointment_time, SEC_TO_TIME(new_duration * 60)) > a.appointment_time
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Dentist already has an overlapping active appointment';
    END IF;
END$$

DROP TRIGGER IF EXISTS trg_prevent_double_booking_update$$
CREATE TRIGGER trg_prevent_double_booking_update
BEFORE UPDATE ON appointments
FOR EACH ROW
BEGIN
    DECLARE new_duration INT DEFAULT 30;
    SELECT duration_minutes INTO new_duration FROM treatments WHERE id = NEW.treatment_id;
    IF NEW.status <> 'CANCELLED' AND EXISTS (
        SELECT 1
        FROM appointments a
        INNER JOIN treatments t ON t.id = a.treatment_id
        WHERE a.id <> NEW.id
          AND a.dentist_id = NEW.dentist_id
          AND a.appointment_date = NEW.appointment_date
          AND a.status <> 'CANCELLED'
          AND NEW.appointment_time < ADDTIME(a.appointment_time, SEC_TO_TIME(t.duration_minutes * 60))
          AND ADDTIME(NEW.appointment_time, SEC_TO_TIME(new_duration * 60)) > a.appointment_time
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Dentist already has an overlapping active appointment';
    END IF;
END$$

DELIMITER ;
