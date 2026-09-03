package lk.sunrise.clinic.controller;

import jakarta.validation.Valid;
import lk.sunrise.clinic.dto.AppointmentForm;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.AppointmentStatus;
import lk.sunrise.clinic.service.AppointmentService;
import lk.sunrise.clinic.service.DentistService;
import lk.sunrise.clinic.service.PatientService;
import lk.sunrise.clinic.service.TreatmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService service;
    private final PatientService patientService;
    private final DentistService dentistService;
    private final TreatmentService treatmentService;

    public AppointmentController(AppointmentService service, PatientService patientService,
                                 DentistService dentistService, TreatmentService treatmentService) {
        this.service = service;
        this.patientService = patientService;
        this.dentistService = dentistService;
        this.treatmentService = treatmentService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("appointments", service.findAll(q));
        model.addAttribute("q", q);
        return "appointments";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("appointmentForm", new AppointmentForm());
        addLookups(model);
        return "appointment-form";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("appointment", service.findById(id));
        return "appointment-details";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("appointmentForm", service.toEditableForm(id));
            addLookups(model);
            return "appointment-form";
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/appointments/" + id;
        }
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("appointmentForm") AppointmentForm form,
                       BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            addLookups(model);
            return "appointment-form";
        }
        try {
            var saved = service.save(form);
            redirectAttributes.addFlashAttribute("success", "Appointment " + saved.getAppointmentNumber() + " saved successfully.");
            return "redirect:/appointments/" + saved.getId();
        } catch (BusinessException ex) {
            model.addAttribute("businessError", ex.getMessage());
            addLookups(model);
            return "appointment-form";
        }
    }

    @PostMapping("/{id}/status")
    public String status(@PathVariable Long id, @RequestParam AppointmentStatus status,
                         RedirectAttributes redirectAttributes) {
        try {
            service.setStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Appointment status updated.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/appointments/" + id;
    }

    private void addLookups(Model model) {
        model.addAttribute("patients", patientService.findAll(null));
        model.addAttribute("dentists", dentistService.findActive());
        model.addAttribute("treatments", treatmentService.findActive());
    }
}
