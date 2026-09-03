package lk.sunrise.clinic.controller;

import lk.sunrise.clinic.service.SystemSetupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    private final SystemSetupService setupService;

    public AuthController(SystemSetupService setupService) {
        this.setupService = setupService;
    }

    @GetMapping("/login")
    public String login() {
        return setupService.setupRequired() ? "redirect:/setup" : "login";
    }

    @GetMapping("/")
    public String root() {
        return setupService.setupRequired() ? "redirect:/setup" : "redirect:/dashboard";
    }
}
