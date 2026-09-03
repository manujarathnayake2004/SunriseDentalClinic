package lk.sunrise.clinic.controller;

import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.service.BillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/billing")
public class BillingController {
    private final BillService billService;

    public BillingController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("bills", billService.findAll());
        model.addAttribute("appointments", billService.findBillableAppointments());
        return "billing";
    }

    @PostMapping("/generate/{appointmentId}")
    public String generate(@PathVariable Long appointmentId, RedirectAttributes redirectAttributes) {
        try {
            var bill = billService.generateForAppointment(appointmentId);
            redirectAttributes.addFlashAttribute("success", "Bill generated successfully.");
            return "redirect:/billing/" + bill.getId();
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/billing";
        }
    }

    @GetMapping("/{id}")
    public String receipt(@PathVariable Long id, Model model) {
        model.addAttribute("bill", billService.findById(id));
        return "receipt";
    }
}
