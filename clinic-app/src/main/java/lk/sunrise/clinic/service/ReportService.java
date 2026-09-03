package lk.sunrise.clinic.service;

import lk.sunrise.clinic.model.Appointment;
import lk.sunrise.clinic.model.AppointmentStatus;
import lk.sunrise.clinic.model.Bill;
import lk.sunrise.clinic.repository.AppointmentRepository;
import lk.sunrise.clinic.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final AdvancedDatabaseFeatureService databaseFeatures;

    public ReportService(AppointmentRepository appointmentRepository, BillRepository billRepository,
                         AdvancedDatabaseFeatureService databaseFeatures) {
        this.appointmentRepository = appointmentRepository;
        this.billRepository = billRepository;
        this.databaseFeatures = databaseFeatures;
    }

    public List<Appointment> appointments(LocalDate from, LocalDate to) {
        return appointmentRepository.findByAppointmentDateBetweenOrderByAppointmentDateAscAppointmentTimeAsc(from, to);
    }

    public List<Bill> bills(LocalDate from, LocalDate to) {
        return billRepository.findByCreatedAtBetweenOrderByCreatedAtAsc(
                from.atStartOfDay(), to.plusDays(1).atStartOfDay().minusNanos(1));
    }

    public BigDecimal revenue(LocalDate from, LocalDate to) {
        return billRepository.sumRevenueBetween(
                from.atStartOfDay(), to.plusDays(1).atStartOfDay().minusNanos(1));
    }

    public List<DentistWorkload> dentistWorkload(LocalDate from, LocalDate to) {
        Map<String, List<Appointment>> grouped = new LinkedHashMap<>();
        for (Appointment appointment : appointments(from, to)) {
            grouped.computeIfAbsent(appointment.getDentist().getDentistName(), key -> new ArrayList<>())
                    .add(appointment);
        }

        return grouped.entrySet().stream()
                .map(entry -> {
                    long completed = entry.getValue().stream()
                            .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED).count();
                    long cancelled = entry.getValue().stream()
                            .filter(a -> a.getStatus() == AppointmentStatus.CANCELLED).count();
                    return new DentistWorkload(entry.getKey(), entry.getValue().size(), completed, cancelled);
                })
                .toList();
    }

    public ReportMetrics metrics(LocalDate from, LocalDate to) {
        List<Appointment> items = appointments(from, to);
        long total = items.size();
        long completed = items.stream().filter(a -> a.getStatus() == AppointmentStatus.COMPLETED).count();
        long cancelled = items.stream().filter(a -> a.getStatus() == AppointmentStatus.CANCELLED).count();
        BigDecimal completionRate = total == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(completed * 100.0 / total).setScale(1, RoundingMode.HALF_UP);
        return new ReportMetrics(total, completed, cancelled, completionRate);
    }

    public List<AdvancedDatabaseFeatureService.DailyAppointmentSummary> dailySummary(LocalDate from, LocalDate to) {
        return databaseFeatures.dailySummary(from, to);
    }

    public List<AdvancedDatabaseFeatureService.AppointmentReportRow> appointmentRowsForExport(LocalDate from, LocalDate to) {
        List<AdvancedDatabaseFeatureService.AppointmentReportRow> rows =
                databaseFeatures.appointmentsBetweenUsingProcedure(from, to);
        if (!rows.isEmpty()) return rows;

        return appointments(from, to).stream()
                .map(a -> new AdvancedDatabaseFeatureService.AppointmentReportRow(
                        a.getAppointmentNumber(),
                        a.getPatient().getPatientName(),
                        a.getDentist().getDentistName(),
                        a.getTreatment().getTreatmentName(),
                        a.getAppointmentDate(),
                        a.getAppointmentTime(),
                        a.getStatus().name()))
                .toList();
    }

    public record DentistWorkload(String dentistName, long totalAppointments, long completed, long cancelled) {}

    public record ReportMetrics(long totalAppointments, long completed, long cancelled, BigDecimal completionRate) {}
}
