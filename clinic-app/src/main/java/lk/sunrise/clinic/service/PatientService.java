package lk.sunrise.clinic.service;

import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.Patient;
import lk.sunrise.clinic.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository repository;
    private final AuditService auditService;

    public PatientService(PatientRepository repository, AuditService auditService) {
        this.repository = repository;
        this.auditService = auditService;
    }

    public List<Patient> findAll(String query) {
        if (query == null || query.isBlank()) return repository.findAll();
        String cleaned = query.trim();
        return repository.findByPatientNameContainingIgnoreCaseOrPatientNumberContainingIgnoreCase(cleaned, cleaned);
    }

    public Patient findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("Patient was not found."));
    }

    @Transactional
    public Patient save(Patient submitted) {
        boolean creating = submitted.getId() == null;
        Patient patient;

        if (creating) {
            patient = new Patient();
            patient.setPatientNumber(nextPatientNumber());
        } else {
            patient = findById(submitted.getId());
        }

        patient.setPatientName(submitted.getPatientName().trim().replaceAll("\\s+", " "));
        patient.setAddress(submitted.getAddress().trim().replaceAll("\\s+", " "));
        patient.setContactNumber(submitted.getContactNumber().trim());

        Patient saved = repository.save(patient);
        auditService.record(creating ? "PATIENT_CREATE" : "PATIENT_UPDATE",
                saved.getPatientNumber() + " - " + saved.getPatientName());
        return saved;
    }

    private String nextPatientNumber() {
        String currentMax = repository.findMaxPatientNumber();
        long next = 1;
        if (currentMax != null && currentMax.startsWith("PAT-")) {
            try {
                next = Long.parseLong(currentMax.substring(4)) + 1;
            } catch (NumberFormatException ignored) {
                next = repository.count() + 1;
            }
        }
        return "PAT-%05d".formatted(next);
    }
}
