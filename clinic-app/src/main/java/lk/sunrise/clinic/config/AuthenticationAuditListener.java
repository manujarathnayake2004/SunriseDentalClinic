package lk.sunrise.clinic.config;

import lk.sunrise.clinic.model.AuditLog;
import lk.sunrise.clinic.repository.AuditLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationAuditListener {
    private final AuditLogRepository repository;

    public AuthenticationAuditListener(AuditLogRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void loginSuccess(AuthenticationSuccessEvent event) {
        save(event.getAuthentication().getName(), "LOGIN_SUCCESS", "Authorized staff login succeeded.");
    }

    @EventListener
    public void loginFailure(AbstractAuthenticationFailureEvent event) {
        save(event.getAuthentication().getName(), "LOGIN_FAILURE", "Login attempt failed.");
    }

    @EventListener
    public void logout(LogoutSuccessEvent event) {
        if (event.getAuthentication() != null) {
            save(event.getAuthentication().getName(), "LOGOUT", "Staff session ended securely.");
        }
    }

    private void save(String username, String action, String description) {
        AuditLog log = new AuditLog();
        log.setUsername(username);
        log.setActionType(action);
        log.setDescription(description);
        repository.save(log);
    }
}
