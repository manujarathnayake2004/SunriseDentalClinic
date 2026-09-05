package lk.sunrise.clinic.service;

import lk.sunrise.clinic.model.Patient;
import lk.sunrise.clinic.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

class PatientServiceTest {
    private PatientRepository repository;
    private AuditService auditService;
    private PatientService service;

    @BeforeEach
    void setUp() {
        repository = mock(PatientRepository.class);
        auditService = mock(AuditService.class);
        service = new PatientService(repository, auditService);
        when(repository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldCreateNextPatientNumberFromHighestExistingNumber() {
        when(repository.findMaxPatientNumber()).thenReturn("PAT-00009");
        Patient patient = new Patient();
        patient.setPatientName("  Test   Patient ");
        patient.setAddress("  Colombo   03 ");
        patient.setContactNumber("0712345678");

        Patient saved = service.save(patient);

        assertEquals("PAT-00010", saved.getPatientNumber());
        assertEquals("Test Patient", saved.getPatientName());
        assertEquals("Colombo 03", saved.getAddress());
        verify(auditService).record(eq("PATIENT_CREATE"), contains("PAT-00010"));
    }
}
