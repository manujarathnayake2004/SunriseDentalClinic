package lk.sunrise.clinic.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lk.sunrise.clinic.dto.InitialAdminForm;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.service.SystemSetupService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SetupController {
    private final SystemSetupService setupService;

    public SetupController(SystemSetupService setupService) {
        this.setupService = setupService;
    }

    @GetMapping("/setup")
    public String setup(HttpServletRequest request, Model model) {
        requireLocalRequest(request);
        if (!setupService.setupRequired()) {
            return "redirect:/login";
        }
        if (!model.containsAttribute("initialAdminForm")) {
            model.addAttribute("initialAdminForm", new InitialAdminForm());
        }
        return "setup";
    }

    @PostMapping("/setup")
    public String save(HttpServletRequest request,
                       @Valid @ModelAttribute("initialAdminForm") InitialAdminForm form,
                       BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        requireLocalRequest(request);
        if (result.hasErrors()) {
            return "setup";
        }
        try {
            setupService.createInitialAdministrator(form);
            redirectAttributes.addFlashAttribute("setupSuccess",
                    "Initial administrator created. Sign in with the account you just created.");
            return "redirect:/login";
        } catch (BusinessException ex) {
            model.addAttribute("businessError", ex.getMessage());
            return "setup";
        }
    }

    private void requireLocalRequest(HttpServletRequest request) {
        String address = request.getRemoteAddr();
        if (!("127.0.0.1".equals(address) || "0:0:0:0:0:0:0:1".equals(address) || "::1".equals(address))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Initial administrator setup is available only from the clinic server computer.");
        }
    }
}
