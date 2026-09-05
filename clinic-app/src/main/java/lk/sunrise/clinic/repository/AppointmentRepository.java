package lk.sunrise.clinic.repository;

import lk.sunrise.clinic.model.Appointment;
import lk.sunrise.clinic.model.AppointmentStatus;
import lk.sunrise.clinic.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByAppointmentNumber(String appointmentNumber);

    boolean existsByDentistAndAppointmentDateAndAppointmentTimeAndStatusNot(
            Dentist dentist, LocalDate date, LocalTime time, AppointmentStatus status);

    boolean existsByDentistAndAppointmentDateAndAppointmentTimeAndStatusNotAndIdNot(
            Dentist dentist, LocalDate date, LocalTime time, AppointmentStatus status, Long id);

    List<Appointment> findByDentistAndAppointmentDateAndStatusNotOrderByAppointmentTimeAsc(
            Dentist dentist, LocalDate date, AppointmentStatus status);

    long countByAppointmentDate(LocalDate date);
    long countByAppointmentDateAndStatus(LocalDate date, AppointmentStatus status);

    List<Appointment> findTop15ByOrderByCreatedAtDesc();
    List<Appointment> findByAppointmentDateOrderByAppointmentTimeAsc(LocalDate date);
    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAscAppointmentTimeAsc(LocalDate from, LocalDate to);

    @Query("select max(a.appointmentNumber) from Appointment a")
    String findMaxAppointmentNumber();

    @Query("""
        select a from Appointment a
        where lower(a.appointmentNumber) like lower(concat('%', :q, '%'))
           or lower(a.patient.patientName) like lower(concat('%', :q, '%'))
           or lower(a.dentist.dentistName) like lower(concat('%', :q, '%'))
        order by a.appointmentDate desc, a.appointmentTime desc
    """)
    List<Appointment> search(@Param("q") String query);
}
