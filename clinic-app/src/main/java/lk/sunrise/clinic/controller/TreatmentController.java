package lk.sunrise.clinic.controller;

import jakarta.validation.Valid;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.Treatment;
import lk.sunrise.clinic.service.TreatmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/treatments")
public class TreatmentController {
    private final TreatmentService service;

    public TreatmentController(TreatmentService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("treatments", service.findAll());
        return "treatments";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("treatment", new Treatment());
        return "treatment-form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("treatment", service.findById(id));
        return "treatment-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Treatment treatment, BindingResult result,
                       Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "treatment-form";
        try {
            service.save(treatment);
            redirectAttributes.addFlashAttribute("success", "Treatment saved successfully.");
            return "redirect:/treatments";
        } catch (BusinessException ex) {
            model.addAttribute("businessError", ex.getMessage());
            return "treatment-form";
        }
    }
}
