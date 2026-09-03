package lk.sunrise.clinic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Installs and uses the advanced database features required by the CIS6003 rubric.
 * The installer is intentionally skipped for non-MySQL/MariaDB databases used by tests.
 */
@Service
public class AdvancedDatabaseFeatureService implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AdvancedDatabaseFeatureService.class);

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private volatile boolean supportedDatabase;

    public AdvancedDatabaseFeatureService(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        supportedDatabase = isMySqlFamily();
        if (!supportedDatabase) {
            log.info("Advanced MySQL/MariaDB database features skipped for this database platform.");
            return;
        }

        try {
            installView();
            installProcedure();
            installFunction();
            installTriggers();
            log.info("Advanced database view, procedure, function and triggers are ready.");
        } catch (Exception ex) {
            // The core application must remain usable if a local DB account lacks CREATE ROUTINE/TRIGGER rights.
            log.warn("Advanced database feature installation was not completed: {}", ex.getMessage());
        }
    }

    public List<DailyAppointmentSummary> dailySummary(LocalDate from, LocalDate to) {
        if (!supportedDatabase) return List.of();
        try {
            return jdbcTemplate.query("""
                    SELECT appointment_date, total_appointments, completed_appointments, cancelled_appointments
                    FROM vw_daily_appointment_summary
                    WHERE appointment_date BETWEEN ? AND ?
                    ORDER BY appointment_date
                    """, (rs, rowNum) -> new DailyAppointmentSummary(
                    rs.getObject("appointment_date", LocalDate.class),
                    rs.getLong("total_appointments"),
                    rs.getLong("completed_appointments"),
                    rs.getLong("cancelled_appointments")), from, to);
        } catch (Exception ex) {
            log.warn("Could not read daily appointment summary view: {}", ex.getMessage());
            return List.of();
        }
    }

    public List<AppointmentReportRow> appointmentsBetweenUsingProcedure(LocalDate from, LocalDate to) {
        if (!supportedDatabase) return List.of();
        try {
            return jdbcTemplate.query("CALL sp_appointments_between_dates(?, ?)",
                    (rs, rowNum) -> new AppointmentReportRow(
                            rs.getString("appointment_number"),
                            rs.getString("patient_name"),
                            rs.getString("dentist_name"),
                            rs.getString("treatment_name"),
                            rs.getObject("appointment_date", LocalDate.class),
                            rs.getObject("appointment_time", LocalTime.class),
                            rs.getString("status")), from, to);
        } catch (Exception ex) {
            log.warn("Could not call appointment reporting procedure: {}", ex.getMessage());
            return List.of();
        }
    }

    public Optional<BigDecimal> calculateTotalUsingFunction(BigDecimal treatmentCost, BigDecimal consultationFee) {
        if (!supportedDatabase) return Optional.empty();
        try {
            BigDecimal value = jdbcTemplate.queryForObject(
                    "SELECT fn_bill_total(?, ?)", BigDecimal.class, treatmentCost, consultationFee);
            return Optional.ofNullable(value);
        } catch (Exception ex) {
            log.warn("Could not call billing database function: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private boolean isMySqlFamily() {
        try (Connection connection = dataSource.getConnection()) {
            String product = connection.getMetaData().getDatabaseProductName().toLowerCase(Locale.ROOT);
            return product.contains("mysql") || product.contains("mariadb");
        } catch (Exception ex) {
            log.warn("Could not detect database platform: {}", ex.getMessage());
            return false;
        }
    }

    private void installView() {
        jdbcTemplate.execute("""
                CREATE OR REPLACE VIEW vw_daily_appointment_summary AS
                SELECT appointment_date,
                       COUNT(*) AS total_appointments,
                       SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_appointments,
                       SUM(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled_appointments
                FROM appointments
                GROUP BY appointment_date
                """);
    }

    private void installProcedure() {
        jdbcTemplate.execute("DROP PROCEDURE IF EXISTS sp_appointments_between_dates");
        jdbcTemplate.execute("""
                CREATE PROCEDURE sp_appointments_between_dates(IN p_from DATE, IN p_to DATE)
                BEGIN
                    SELECT a.appointment_number,
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
                END
                """);
    }

    private void installFunction() {
        jdbcTemplate.execute("DROP FUNCTION IF EXISTS fn_bill_total");
        jdbcTemplate.execute("""
                CREATE FUNCTION fn_bill_total(p_treatment DECIMAL(12,2), p_consultation DECIMAL(12,2))
                RETURNS DECIMAL(12,2)
                DETERMINISTIC
                RETURN COALESCE(p_treatment, 0) + COALESCE(p_consultation, 0)
                """);
    }

    private void installTriggers() {
        jdbcTemplate.execute("DROP TRIGGER IF EXISTS trg_prevent_double_booking_insert");
        jdbcTemplate.execute("""
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
                END
                """);

        jdbcTemplate.execute("DROP TRIGGER IF EXISTS trg_prevent_double_booking_update");
        jdbcTemplate.execute("""
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
                END
                """);
    }

    public record DailyAppointmentSummary(LocalDate date, long total, long completed, long cancelled) {}

    public record AppointmentReportRow(
            String appointmentNumber,
            String patientName,
            String dentistName,
            String treatmentName,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            String status) {}
}
