package lk.sunrise.clinic.service;

import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.Dentist;
import lk.sunrise.clinic.repository.DentistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DentistService {
    private final DentistRepository repository;
    private final AuditService auditService;

    public DentistService(DentistRepository repository, AuditService auditService) {
        this.repository = repository;
        this.auditService = auditService;
    }

    public List<Dentist> findAll() { return repository.findAll(); }
    public List<Dentist> findActive() { return repository.findByActiveTrueOrderByDentistNameAsc(); }

    public Dentist findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("Dentist was not found."));
    }

    @Transactional
    public Dentist save(Dentist submitted) {
        boolean creating = submitted.getId() == null;
        String name = submitted.getDentistName().trim().replaceAll("\\s+", " ");
        String specialization = submitted.getSpecialization().trim().replaceAll("\\s+", " ");

        if (creating ? repository.existsByDentistNameIgnoreCase(name)
                : repository.existsByDentistNameIgnoreCaseAndIdNot(name, submitted.getId())) {
            throw new BusinessException("A dentist with this name already exists.");
        }

        Dentist dentist = creating ? new Dentist() : findById(submitted.getId());
        dentist.setDentistName(name);
        dentist.setSpecialization(specialization);
        dentist.setActive(submitted.isActive());

        Dentist saved = repository.save(dentist);
        auditService.record(creating ? "DENTIST_CREATE" : "DENTIST_UPDATE", saved.getDentistName());
        return saved;
    }
}
