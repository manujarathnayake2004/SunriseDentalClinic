package lk.sunrise.clinic.controller;

import jakarta.validation.Valid;
import lk.sunrise.clinic.dto.UserForm;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.Role;
import lk.sunrise.clinic.service.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController {
    private final UserAccountService service;

    public UserAdminController(UserAccountService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", service.findAll());
        return "users";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("roles", Role.values());
        return "user-form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("userForm", service.toForm(service.findById(id)));
        model.addAttribute("roles", Role.values());
        return "user-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("userForm") UserForm form, BindingResult result,
                       Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "user-form";
        }
        try {
            service.save(form);
            redirectAttributes.addFlashAttribute("success", "Staff user saved successfully.");
            return "redirect:/admin/users";
        } catch (BusinessException ex) {
            model.addAttribute("businessError", ex.getMessage());
            model.addAttribute("roles", Role.values());
            return "user-form";
        }
    }
}
