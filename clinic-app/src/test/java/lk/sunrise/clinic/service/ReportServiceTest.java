package lk.sunrise.clinic.service;

import lk.sunrise.clinic.model.Appointment;
import lk.sunrise.clinic.model.AppointmentStatus;
import lk.sunrise.clinic.repository.AppointmentRepository;
import lk.sunrise.clinic.repository.BillRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReportServiceTest {
    @Test
    void shouldCalculateCompletionMetricsForDecisionMaking() {
        AppointmentRepository appointments = mock(AppointmentRepository.class);
        BillRepository bills = mock(BillRepository.class);
        AdvancedDatabaseFeatureService databaseFeatures = mock(AdvancedDatabaseFeatureService.class);
        ReportService service = new ReportService(appointments, bills, databaseFeatures);

        Appointment completed1 = appointment(AppointmentStatus.COMPLETED);
        Appointment completed2 = appointment(AppointmentStatus.COMPLETED);
        Appointment cancelled = appointment(AppointmentStatus.CANCELLED);
        Appointment scheduled = appointment(AppointmentStatus.SCHEDULED);
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();
        when(appointments.findByAppointmentDateBetweenOrderByAppointmentDateAscAppointmentTimeAsc(from, to))
                .thenReturn(List.of(completed1, completed2, cancelled, scheduled));

        var metrics = service.metrics(from, to);

        assertEquals(4, metrics.totalAppointments());
        assertEquals(2, metrics.completed());
        assertEquals(1, metrics.cancelled());
        assertEquals("50.0", metrics.completionRate().toPlainString());
    }

    private Appointment appointment(AppointmentStatus status) {
        Appointment appointment = new Appointment();
        appointment.setStatus(status);
        return appointment;
    }
}
