package lk.sunrise.clinic.service;

import lk.sunrise.clinic.model.Appointment;
import lk.sunrise.clinic.model.AppointmentStatus;
import lk.sunrise.clinic.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ReminderService {
    private static final ZoneId CLINIC_ZONE = ZoneId.of("Asia/Colombo");
    private final AppointmentRepository repository;

    public ReminderService(AppointmentRepository repository) {
        this.repository = repository;
    }

    public List<Appointment> upcomingWithinHours(long hours) {
        LocalDateTime now = LocalDateTime.now(CLINIC_ZONE);
        LocalDateTime until = now.plusHours(hours);
        return repository.findByAppointmentDateBetweenOrderByAppointmentDateAscAppointmentTimeAsc(
                        now.toLocalDate(), until.toLocalDate()).stream()
                .filter(a -> a.getStatus() == AppointmentStatus.SCHEDULED)
                .filter(a -> {
                    LocalDateTime at = LocalDateTime.of(a.getAppointmentDate(), a.getAppointmentTime());
                    return !at.isBefore(now) && !at.isAfter(until);
                })
                .limit(8)
                .toList();
    }
}
