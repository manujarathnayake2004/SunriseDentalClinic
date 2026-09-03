package lk.sunrise.clinic.controller;

import lk.sunrise.clinic.repository.AuditLogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuditController {
    private final AuditLogRepository repository;

    public AuditController(AuditLogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/admin/audit")
    public String audit(Model model) {
        model.addAttribute("logs", repository.findTop200ByOrderByCreatedAtDesc());
        return "audit";
    }
}
