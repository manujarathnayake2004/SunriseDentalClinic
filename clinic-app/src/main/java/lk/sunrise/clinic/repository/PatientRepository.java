package lk.sunrise.clinic.repository;

import lk.sunrise.clinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPatientNumber(String patientNumber);
    List<Patient> findTop8ByOrderByCreatedAtDesc();
    List<Patient> findByPatientNameContainingIgnoreCaseOrPatientNumberContainingIgnoreCase(String name, String number);

    @Query("select max(p.patientNumber) from Patient p")
    String findMaxPatientNumber();
}
