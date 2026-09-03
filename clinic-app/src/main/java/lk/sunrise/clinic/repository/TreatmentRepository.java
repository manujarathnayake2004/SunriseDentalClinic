package lk.sunrise.clinic.repository;

import lk.sunrise.clinic.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    List<Treatment> findByActiveTrueOrderByTreatmentNameAsc();
    boolean existsByTreatmentNameIgnoreCase(String treatmentName);
    boolean existsByTreatmentNameIgnoreCaseAndIdNot(String treatmentName, Long id);
}
