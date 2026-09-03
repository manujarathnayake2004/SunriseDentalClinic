package lk.sunrise.clinic.controller;

import jakarta.validation.Valid;
import lk.sunrise.clinic.model.Patient;
import lk.sunrise.clinic.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patients")
public class PatientController {
    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("patients", service.findAll(q));
        model.addAttribute("q", q);
        return "patients";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient-form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("patient", service.findById(id));
        return "patient-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Patient patient, BindingResult result,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "patient-form";
        service.save(patient);
        redirectAttributes.addFlashAttribute("success", "Patient saved successfully.");
        return "redirect:/patients";
    }
}
