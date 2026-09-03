package lk.sunrise.clinic.controller;

import lk.sunrise.clinic.model.AppointmentStatus;
import lk.sunrise.clinic.repository.*;
import lk.sunrise.clinic.service.ReminderService;
import lk.sunrise.clinic.service.BillingClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

@Controller
public class DashboardController {
    private static final ZoneId CLINIC_ZONE = ZoneId.of("Asia/Colombo");

    private final PatientRepository patients;
    private final DentistRepository dentists;
    private final AppointmentRepository appointments;
    private final BillRepository bills;
    private final ReminderService reminderService;
    private final BillingClient billingClient;

    public DashboardController(PatientRepository patients, DentistRepository dentists,
                               AppointmentRepository appointments, BillRepository bills,
                               ReminderService reminderService, BillingClient billingClient) {
        this.patients = patients;
        this.dentists = dentists;
        this.appointments = appointments;
        this.bills = bills;
        this.reminderService = reminderService;
        this.billingClient = billingClient;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        LocalDate today = LocalDate.now(CLINIC_ZONE);
        BigDecimal todayRevenue = bills.sumRevenueBetween(today.atStartOfDay(), today.plusDays(1).atStartOfDay().minusNanos(1));

        model.addAttribute("totalPatients", patients.count());
        model.addAttribute("activeDentists", dentists.findByActiveTrueOrderByDentistNameAsc().size());
        model.addAttribute("todayAppointments", appointments.countByAppointmentDate(today));
        model.addAttribute("todayCompleted", appointments.countByAppointmentDateAndStatus(today, AppointmentStatus.COMPLETED));
        model.addAttribute("todayRevenue", todayRevenue);
        model.addAttribute("recentAppointments", appointments.findTop8ByOrderByCreatedAtDesc());
        model.addAttribute("recentBills", bills.findTop8ByOrderByCreatedAtDesc());
        model.addAttribute("upcomingReminders", reminderService.upcomingWithinHours(24));
        model.addAttribute("billingServiceOnline", billingClient.isAvailable());
        return "dashboard";
    }
}
