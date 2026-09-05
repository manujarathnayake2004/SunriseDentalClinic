package lk.sunrise.clinic.service;

import lk.sunrise.clinic.dto.BillingQuoteResponse;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.*;
import lk.sunrise.clinic.repository.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BillServiceTest {
    private BillRepository repository;
    private AppointmentService appointmentService;
    private BillingClient billingClient;
    private AuditService auditService;
    private AdvancedDatabaseFeatureService databaseFeatures;
    private BillService service;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        repository = mock(BillRepository.class);
        appointmentService = mock(AppointmentService.class);
        billingClient = mock(BillingClient.class);
        auditService = mock(AuditService.class);
        databaseFeatures = mock(AdvancedDatabaseFeatureService.class);
        service = new BillService(repository, appointmentService, billingClient, auditService, databaseFeatures);

        Patient patient = new Patient();
        patient.setPatientName("Test Patient");

        Treatment treatment = new Treatment();
        treatment.setTreatmentName("Test Treatment");
        treatment.setTreatmentCost(new BigDecimal("4000.00"));
        treatment.setConsultationFee(new BigDecimal("1500.00"));

        appointment = new Appointment();
        appointment.setAppointmentNumber("APP-TEST-1");
        appointment.setPatient(patient);
        appointment.setTreatment(treatment);

        when(appointmentService.findById(1L)).thenReturn(appointment);
        when(databaseFeatures.calculateTotalUsingFunction(any(), any())).thenReturn(Optional.empty());
    }

    @Test
    void shouldRejectCancelledAppointment() {
        appointment.setStatus(AppointmentStatus.CANCELLED);
        BusinessException ex = assertThrows(BusinessException.class, () -> service.generateForAppointment(1L));
        assertTrue(ex.getMessage().contains("cancelled"));
        verifyNoInteractions(billingClient);
    }

    @Test
    void shouldRequireCompletedAppointmentBeforeBilling() {
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        BusinessException ex = assertThrows(BusinessException.class, () -> service.generateForAppointment(1L));
        assertTrue(ex.getMessage().contains("completed"));
        verifyNoInteractions(billingClient);
    }

    @Test
    void shouldCreateBillFromBillingWebServiceQuote() {
        appointment.setStatus(AppointmentStatus.COMPLETED);
        when(repository.findByAppointment(appointment)).thenReturn(Optional.empty());
        when(repository.findMaxBillNumber()).thenReturn(null);
        when(billingClient.calculate(new BigDecimal("4000.00"), new BigDecimal("1500.00")))
                .thenReturn(new BillingQuoteResponse(new BigDecimal("4000.00"), new BigDecimal("1500.00"), new BigDecimal("5500.00")));
        when(databaseFeatures.calculateTotalUsingFunction(any(), any())).thenReturn(Optional.of(new BigDecimal("5500.00")));
        when(repository.saveAndFlush(any(Bill.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bill bill = service.generateForAppointment(1L);

        assertEquals("BILL-000001", bill.getBillNumber());
        assertEquals(new BigDecimal("5500.00"), bill.getTotalAmount());
        verify(repository).saveAndFlush(any(Bill.class));
        verify(auditService).record(eq("BILL_CREATE"), contains("APP-TEST-1"));
    }

    @Test
    void shouldRejectBillingWhenWebServiceAndDatabaseFunctionDisagree() {
        appointment.setStatus(AppointmentStatus.COMPLETED);
        when(repository.findByAppointment(appointment)).thenReturn(Optional.empty());
        when(billingClient.calculate(any(), any())).thenReturn(
                new BillingQuoteResponse(new BigDecimal("4000.00"), new BigDecimal("1500.00"), new BigDecimal("5500.00")));
        when(databaseFeatures.calculateTotalUsingFunction(any(), any())).thenReturn(Optional.of(new BigDecimal("5600.00")));

        BusinessException ex = assertThrows(BusinessException.class, () -> service.generateForAppointment(1L));

        assertTrue(ex.getMessage().contains("verification failed"));
        verify(repository, never()).saveAndFlush(any(Bill.class));
    }

    @Test
    void shouldReturnExistingBillWithoutCallingWebServiceAgain() {
        appointment.setStatus(AppointmentStatus.COMPLETED);
        Bill existing = new Bill();
        existing.setAppointment(appointment);
        existing.setBillNumber("BILL-000007");
        when(repository.findByAppointment(appointment)).thenReturn(Optional.of(existing));

        Bill result = service.generateForAppointment(1L);

        assertSame(existing, result);
        verifyNoInteractions(billingClient);
    }
}
