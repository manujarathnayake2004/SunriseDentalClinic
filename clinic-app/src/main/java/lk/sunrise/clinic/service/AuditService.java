package lk.sunrise.clinic.service;

import lk.sunrise.clinic.model.AuditLog;
import lk.sunrise.clinic.repository.AuditLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void record(String action, String description) {
        AuditLog log = new AuditLog();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            log.setUsername(auth.getName());
        }
        log.setActionType(action);
        log.setDescription(description);
        repository.save(log);
    }
}
