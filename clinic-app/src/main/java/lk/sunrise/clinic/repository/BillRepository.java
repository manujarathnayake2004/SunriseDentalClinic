package lk.sunrise.clinic.repository;

import lk.sunrise.clinic.model.Appointment;
import lk.sunrise.clinic.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByAppointment(Appointment appointment);
    List<Bill> findTop8ByOrderByCreatedAtDesc();
    List<Bill> findByCreatedAtBetweenOrderByCreatedAtAsc(LocalDateTime from, LocalDateTime to);

    @Query("select max(b.billNumber) from Bill b")
    String findMaxBillNumber();

    @Query("select coalesce(sum(b.totalAmount), 0) from Bill b where b.createdAt between :from and :to")
    BigDecimal sumRevenueBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
