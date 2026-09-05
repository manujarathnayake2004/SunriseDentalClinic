package lk.sunrise.clinic.service;

import lk.sunrise.clinic.dto.AppointmentForm;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.*;
import lk.sunrise.clinic.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {
    private AppointmentRepository repository;
    private PatientService patientService;
    private DentistService dentistService;
    private TreatmentService treatmentService;
    private AuditService auditService;
    private AppointmentService service;

    private Dentist dentist;
    private Treatment treatment;
    private Patient patient;

    @BeforeEach
    void setUp() {
        repository = mock(AppointmentRepository.class);
        patientService = mock(PatientService.class);
        dentistService = mock(DentistService.class);
        treatmentService = mock(TreatmentService.class);
        auditService = mock(AuditService.class);
        service = new AppointmentService(repository, patientService, dentistService, treatmentService, auditService);

        patient = new Patient();
        patient.setPatientNumber("PAT-00001");
        patient.setPatientName("Test Patient");
        patient.setAddress("Colombo");
        patient.setContactNumber("0712345678");

        dentist = new Dentist();
        dentist.setId(1L);
        dentist.setDentistName("Dr Test");
        dentist.setSpecialization("General Dentistry");
        dentist.setActive(true);

        treatment = new Treatment();
        treatment.setId(1L);
        treatment.setTreatmentName("Dental Cleaning");
        treatment.setTreatmentCost(new BigDecimal("5000.00"));
        treatment.setConsultationFee(new BigDecimal("2000.00"));
        treatment.setDurationMinutes(45);
        treatment.setActive(true);

        when(patientService.findById(1L)).thenReturn(patient);
        when(dentistService.findById(1L)).thenReturn(dentist);
        when(treatmentService.findById(1L)).thenReturn(treatment);
        when(repository.findByDentistAndAppointmentDateAndStatusNotOrderByAppointmentTimeAsc(any(), any(), any()))
                .thenReturn(List.of());
        when(repository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldCreateAppointmentWhenSlotIsAvailable() {
        when(repository.findMaxAppointmentNumber()).thenReturn(null);

        var saved = service.save(validForm());

        assertNotNull(saved);
        assertEquals("APP-000001", saved.getAppointmentNumber());
        assertEquals(patient, saved.getPatient());
        assertEquals(dentist, saved.getDentist());
        assertEquals(treatment, saved.getTreatment());
        verify(repository).saveAndFlush(any());
    }

    @Test
    void shouldContinueAppointmentNumbersWithoutReusingDeletedNumbers() {
        when(repository.findMaxAppointmentNumber()).thenReturn("APP-000009");

        var saved = service.save(validForm());

        assertEquals("APP-000010", saved.getAppointmentNumber());
    }

    @Test
    void shouldRejectOverlappingDentistBookingUsingTreatmentDuration() {
        Appointment existing = new Appointment();
        existing.setAppointmentNumber("APP-000001");
        existing.setDentist(dentist);
        existing.setTreatment(treatment);
        existing.setAppointmentDate(LocalDate.now().plusDays(1));
        existing.setAppointmentTime(LocalTime.of(10, 0));
        existing.setStatus(AppointmentStatus.SCHEDULED);

        when(repository.findByDentistAndAppointmentDateAndStatusNotOrderByAppointmentTimeAsc(any(), any(), any()))
                .thenReturn(List.of(existing));

        AppointmentForm form = validForm();
        form.setAppointmentTime(LocalTime.of(10, 30));

        BusinessException ex = assertThrows(BusinessException.class, () -> service.save(form));

        assertTrue(ex.getMessage().contains("already booked"));
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void shouldRejectAppointmentOutsideClinicHours() {
        AppointmentForm form = validForm();
        form.setAppointmentTime(LocalTime.of(7, 30));

        BusinessException ex = assertThrows(BusinessException.class, () -> service.save(form));

        assertTrue(ex.getMessage().contains("08:00"));
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void shouldRejectTreatmentThatWouldFinishAfterClosing() {
        treatment.setDurationMinutes(60);
        AppointmentForm form = validForm();
        form.setAppointmentTime(LocalTime.of(17, 30));

        BusinessException ex = assertThrows(BusinessException.class, () -> service.save(form));

        assertTrue(ex.getMessage().contains("18:00"));
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void shouldRejectAppointmentTimeOutsideFifteenMinuteIntervals() {
        AppointmentForm form = validForm();
        form.setAppointmentTime(LocalTime.of(10, 10));

        BusinessException ex = assertThrows(BusinessException.class, () -> service.save(form));

        assertTrue(ex.getMessage().contains("15-minute"));
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void shouldRejectAppointmentInThePast() {
        AppointmentForm form = validForm();
        form.setAppointmentDate(LocalDate.now().minusDays(1));
        form.setAppointmentTime(LocalTime.of(10, 0));

        BusinessException ex = assertThrows(BusinessException.class, () -> service.save(form));

        assertTrue(ex.getMessage().contains("cannot be in the past"));
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void shouldRejectInactiveDentist() {
        dentist.setActive(false);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.save(validForm()));

        assertTrue(ex.getMessage().contains("inactive"));
        verify(repository, never()).saveAndFlush(any());
    }

    private AppointmentForm validForm() {
        AppointmentForm form = new AppointmentForm();
        form.setPatientId(1L);
        form.setDentistId(1L);
        form.setTreatmentId(1L);
        form.setAppointmentDate(LocalDate.now().plusDays(1));
        form.setAppointmentTime(LocalTime.of(10, 0));
        form.setNotes("Test appointment");
        return form;
    }
}
