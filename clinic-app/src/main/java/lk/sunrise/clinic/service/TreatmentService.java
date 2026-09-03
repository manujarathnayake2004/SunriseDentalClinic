package lk.sunrise.clinic.service;

import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.Treatment;
import lk.sunrise.clinic.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TreatmentService {
    private final TreatmentRepository repository;
    private final AuditService auditService;

    public TreatmentService(TreatmentRepository repository, AuditService auditService) {
        this.repository = repository;
        this.auditService = auditService;
    }

    public List<Treatment> findAll() { return repository.findAll(); }
    public List<Treatment> findActive() { return repository.findByActiveTrueOrderByTreatmentNameAsc(); }

    public Treatment findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("Treatment was not found."));
    }

    @Transactional
    public Treatment save(Treatment submitted) {
        boolean creating = submitted.getId() == null;
        String name = submitted.getTreatmentName().trim().replaceAll("\\s+", " ");

        if (creating ? repository.existsByTreatmentNameIgnoreCase(name)
                : repository.existsByTreatmentNameIgnoreCaseAndIdNot(name, submitted.getId())) {
            throw new BusinessException("A treatment with this name already exists.");
        }

        Treatment treatment = creating ? new Treatment() : findById(submitted.getId());
        treatment.setTreatmentName(name);
        treatment.setTreatmentCost(submitted.getTreatmentCost());
        treatment.setConsultationFee(submitted.getConsultationFee());
        treatment.setDurationMinutes(submitted.getDurationMinutes());
        treatment.setActive(submitted.isActive());

        Treatment saved = repository.save(treatment);
        auditService.record(creating ? "TREATMENT_CREATE" : "TREATMENT_UPDATE", saved.getTreatmentName());
        return saved;
    }
}
