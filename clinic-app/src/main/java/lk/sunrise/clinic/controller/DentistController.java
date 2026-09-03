package lk.sunrise.clinic.controller;

import jakarta.validation.Valid;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.Dentist;
import lk.sunrise.clinic.service.DentistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dentists")
public class DentistController {
    private final DentistService service;

    public DentistController(DentistService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("dentists", service.findAll());
        return "dentists";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("dentist", new Dentist());
        return "dentist-form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("dentist", service.findById(id));
        return "dentist-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Dentist dentist, BindingResult result,
                       Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "dentist-form";
        try {
            service.save(dentist);
            redirectAttributes.addFlashAttribute("success", "Dentist saved successfully.");
            return "redirect:/dentists";
        } catch (BusinessException ex) {
            model.addAttribute("businessError", ex.getMessage());
            return "dentist-form";
        }
    }
}
