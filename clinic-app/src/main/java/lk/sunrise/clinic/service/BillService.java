package lk.sunrise.clinic.service;

import lk.sunrise.clinic.dto.BillingQuoteResponse;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.Appointment;
import lk.sunrise.clinic.model.AppointmentStatus;
import lk.sunrise.clinic.model.Bill;
import lk.sunrise.clinic.repository.BillRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BillService {
    private final BillRepository repository;
    private final AppointmentService appointmentService;
    private final BillingClient billingClient;
    private final AuditService auditService;
    private final AdvancedDatabaseFeatureService databaseFeatures;

    public BillService(BillRepository repository, AppointmentService appointmentService,
                       BillingClient billingClient, AuditService auditService,
                       AdvancedDatabaseFeatureService databaseFeatures) {
        this.repository = repository;
        this.appointmentService = appointmentService;
        this.billingClient = billingClient;
        this.auditService = auditService;
        this.databaseFeatures = databaseFeatures;
    }

    public List<Bill> findAll() {
        return repository.findAll();
    }

    public List<Appointment> findBillableAppointments() {
        return appointmentService.findAll(null).stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                .filter(a -> repository.findByAppointment(a).isEmpty())
                .toList();
    }

    public Bill findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("Bill was not found."));
    }

    @Transactional
    public Bill generateForAppointment(Long appointmentId) {
        Appointment appointment = appointmentService.findById(appointmentId);
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BusinessException("A cancelled appointment cannot be billed.");
        }
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new BusinessException("Mark the appointment as completed before generating the bill.");
        }

        return repository.findByAppointment(appointment).orElseGet(() -> {
            BigDecimal treatmentCost = appointment.getTreatment().getTreatmentCost();
            BigDecimal consultationFee = appointment.getTreatment().getConsultationFee();

            BillingQuoteResponse quote = billingClient.calculate(treatmentCost, consultationFee);
            if (quote.treatmentCost() == null || quote.consultationFee() == null || quote.total() == null) {
                throw new BusinessException("Billing web service returned incomplete billing data.");
            }
            if (quote.total().signum() < 0) {
                throw new BusinessException("Billing web service returned an invalid total.");
            }

            // Cross-check the distributed web-service result with the DB function when MySQL/MariaDB supports it.
            databaseFeatures.calculateTotalUsingFunction(treatmentCost, consultationFee)
                    .ifPresent(databaseTotal -> {
                        if (databaseTotal.compareTo(quote.total()) != 0) {
                            throw new BusinessException("Billing verification failed. Web-service and database totals do not match.");
                        }
                    });

            Bill bill = new Bill();
            bill.setBillNumber(nextBillNumber());
            bill.setAppointment(appointment);
            bill.setTreatmentCost(quote.treatmentCost());
            bill.setConsultationFee(quote.consultationFee());
            bill.setTotalAmount(quote.total());
            try {
                Bill saved = repository.saveAndFlush(bill);
                auditService.record("BILL_CREATE", saved.getBillNumber() + " for " + appointment.getAppointmentNumber());
                return saved;
            } catch (DataIntegrityViolationException ex) {
                throw new BusinessException("A bill already exists for this appointment or the bill could not be stored safely.");
            }
        });
    }

    private String nextBillNumber() {
        String currentMax = repository.findMaxBillNumber();
        long next = 1;
        if (currentMax != null && currentMax.startsWith("BILL-")) {
            try {
                next = Long.parseLong(currentMax.substring(5)) + 1;
            } catch (NumberFormatException ignored) {
                next = repository.count() + 1;
            }
        }
        return "BILL-%06d".formatted(next);
    }
}
