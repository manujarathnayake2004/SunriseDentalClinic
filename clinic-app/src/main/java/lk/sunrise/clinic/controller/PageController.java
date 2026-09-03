package lk.sunrise.clinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/help")
    public String help() {
        return "help";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("statusCode", 403);
        model.addAttribute("errorTitle", "Access denied");
        model.addAttribute("errorMessage", "Your staff account does not have permission to open that administration function.");
        return "error";
    }
}
