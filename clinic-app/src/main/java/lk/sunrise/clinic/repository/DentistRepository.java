package lk.sunrise.clinic.repository;

import lk.sunrise.clinic.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DentistRepository extends JpaRepository<Dentist, Long> {
    List<Dentist> findByActiveTrueOrderByDentistNameAsc();
    boolean existsByDentistNameIgnoreCase(String dentistName);
    boolean existsByDentistNameIgnoreCaseAndIdNot(String dentistName, Long id);
}
