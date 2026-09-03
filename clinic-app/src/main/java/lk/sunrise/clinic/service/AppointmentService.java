package lk.sunrise.clinic.service;

import lk.sunrise.clinic.dto.AppointmentForm;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.*;
import lk.sunrise.clinic.repository.AppointmentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AppointmentService {
    private static final ZoneId CLINIC_ZONE = ZoneId.of("Asia/Colombo");
    private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    private static final LocalTime LAST_APPOINTMENT_TIME = LocalTime.of(17, 30);
    private static final LocalTime CLOSING_TIME = LocalTime.of(18, 0);

    private final AppointmentRepository repository;
    private final PatientService patientService;
    private final DentistService dentistService;
    private final TreatmentService treatmentService;
    private final AuditService auditService;

    public AppointmentService(AppointmentRepository repository,
                              PatientService patientService,
                              DentistService dentistService,
                              TreatmentService treatmentService,
                              AuditService auditService) {
        this.repository = repository;
        this.patientService = patientService;
        this.dentistService = dentistService;
        this.treatmentService = treatmentService;
        this.auditService = auditService;
    }

    public List<Appointment> findAll(String query) {
        if (query == null || query.isBlank()) return repository.findAll();
        return repository.search(query.trim());
    }

    public List<Appointment> findToday() {
        return repository.findByAppointmentDateOrderByAppointmentTimeAsc(LocalDate.now(CLINIC_ZONE));
    }

    public Appointment findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("Appointment was not found."));
    }

    public Appointment findByNumber(String number) {
        if (number == null || number.isBlank()) {
            throw new BusinessException("Enter an appointment number to search.");
        }
        return repository.findByAppointmentNumber(number.trim())
                .orElseThrow(() -> new BusinessException("No appointment found for number " + number.trim() + "."));
    }

    public AppointmentForm toForm(Appointment a) {
        AppointmentForm f = new AppointmentForm();
        f.setId(a.getId());
        f.setPatientId(a.getPatient().getId());
        f.setDentistId(a.getDentist().getId());
        f.setTreatmentId(a.getTreatment().getId());
        f.setAppointmentDate(a.getAppointmentDate());
        f.setAppointmentTime(a.getAppointmentTime());
        f.setNotes(a.getNotes());
        return f;
    }

    public AppointmentForm toEditableForm(Long id) {
        Appointment appointment = findById(id);
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new BusinessException("Only scheduled appointments can be edited.");
        }
        return toForm(appointment);
    }

    @Transactional
    public Appointment save(AppointmentForm form) {
        Patient patient = patientService.findById(form.getPatientId());
        Dentist dentist = dentistService.findById(form.getDentistId());
        Treatment treatment = treatmentService.findById(form.getTreatmentId());

        if (!dentist.isActive()) throw new BusinessException("The selected dentist is inactive.");
        if (!treatment.isActive()) throw new BusinessException("The selected treatment is inactive.");
        validateAppointmentDateTime(form.getAppointmentDate(), form.getAppointmentTime(), treatment.getDurationMinutes());

        Appointment appointment;
        boolean creating = form.getId() == null;
        Long excludedAppointmentId = null;

        if (creating) {
            appointment = new Appointment();
            appointment.setAppointmentNumber(nextAppointmentNumber());
        } else {
            appointment = findById(form.getId());
            if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
                throw new BusinessException("Only scheduled appointments can be edited.");
            }
            excludedAppointmentId = appointment.getId();
        }

        ensureDentistHasNoOverlap(dentist, form.getAppointmentDate(), form.getAppointmentTime(),
                treatment.getDurationMinutes(), excludedAppointmentId);

        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setTreatment(treatment);
        appointment.setAppointmentDate(form.getAppointmentDate());
        appointment.setAppointmentTime(form.getAppointmentTime());
        appointment.setNotes(form.getNotes() == null ? null : form.getNotes().trim());

        try {
            Appointment saved = repository.saveAndFlush(appointment);
            auditService.record(creating ? "APPOINTMENT_CREATE" : "APPOINTMENT_UPDATE",
                    saved.getAppointmentNumber() + " - " + saved.getPatient().getPatientName());
            return saved;
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("The appointment could not be saved because it conflicts with an existing appointment or database rule.");
        }
    }

    @Transactional
    public void setStatus(Long id, AppointmentStatus status) {
        if (status == null) throw new BusinessException("Appointment status is required.");

        Appointment appointment = findById(id);
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED && appointment.getStatus() != status) {
            throw new BusinessException("Completed or cancelled appointments cannot be changed to another status.");
        }

        if (status == AppointmentStatus.SCHEDULED) {
            ensureDentistHasNoOverlap(appointment.getDentist(), appointment.getAppointmentDate(),
                    appointment.getAppointmentTime(), appointment.getTreatment().getDurationMinutes(), appointment.getId());
        }

        appointment.setStatus(status);
        repository.save(appointment);
        auditService.record("APPOINTMENT_STATUS", appointment.getAppointmentNumber() + " -> " + status);
    }

    private void ensureDentistHasNoOverlap(Dentist dentist, LocalDate date, LocalTime start,
                                           int durationMinutes, Long excludedAppointmentId) {
        LocalTime end = start.plusMinutes(durationMinutes);
        List<Appointment> activeAppointments = repository
                .findByDentistAndAppointmentDateAndStatusNotOrderByAppointmentTimeAsc(
                        dentist, date, AppointmentStatus.CANCELLED);

        for (Appointment existing : activeAppointments) {
            if (excludedAppointmentId != null && excludedAppointmentId.equals(existing.getId())) continue;

            LocalTime existingStart = existing.getAppointmentTime();
            LocalTime existingEnd = existingStart.plusMinutes(existing.getTreatment().getDurationMinutes());
            boolean overlaps = start.isBefore(existingEnd) && existingStart.isBefore(end);
            if (overlaps) {
                throw new BusinessException("This dentist is already booked from " + existingStart + " to " + existingEnd
                        + ". Choose another time or dentist.");
            }
        }
    }

    private String nextAppointmentNumber() {
        return nextFixedNumber(repository.findMaxAppointmentNumber(), "APP-", 6);
    }

    private String nextFixedNumber(String currentMax, String prefix, int digits) {
        long next = 1;
        if (currentMax != null && currentMax.startsWith(prefix)) {
            try {
                next = Long.parseLong(currentMax.substring(prefix.length())) + 1;
            } catch (NumberFormatException ignored) {
                next = repository.count() + 1;
            }
        }
        return prefix + String.format("%0" + digits + "d", next);
    }

    private void validateAppointmentDateTime(LocalDate date, LocalTime time, int durationMinutes) {
        if (date == null || time == null) {
            throw new BusinessException("Appointment date and time are required.");
        }
        if (time.isBefore(OPENING_TIME) || time.isAfter(LAST_APPOINTMENT_TIME)) {
            throw new BusinessException("Appointments can start between 08:00 and 17:30.");
        }
        if (time.getMinute() % 15 != 0) {
            throw new BusinessException("Appointment times must use 15-minute intervals.");
        }
        if (time.plusMinutes(durationMinutes).isAfter(CLOSING_TIME)) {
            throw new BusinessException("The selected treatment would finish after the clinic closes at 18:00.");
        }
        LocalDateTime selected = LocalDateTime.of(date, time);
        if (selected.isBefore(LocalDateTime.now(CLINIC_ZONE))) {
            throw new BusinessException("Appointment date and time cannot be in the past.");
        }
    }
}
