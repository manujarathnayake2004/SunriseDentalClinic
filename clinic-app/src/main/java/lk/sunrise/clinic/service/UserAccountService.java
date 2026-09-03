package lk.sunrise.clinic.service;

import lk.sunrise.clinic.dto.UserForm;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.Role;
import lk.sunrise.clinic.model.UserAccount;
import lk.sunrise.clinic.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAccountService {
    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    public UserAccountService(UserAccountRepository repository, PasswordEncoder passwordEncoder, AuditService auditService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }

    public List<UserAccount> findAll() {
        return repository.findAll();
    }

    public UserAccount findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("Staff user was not found."));
    }

    public UserForm toForm(UserAccount user) {
        UserForm form = new UserForm();
        form.setId(user.getId());
        form.setFullName(user.getFullName());
        form.setUsername(user.getUsername());
        form.setRole(user.getRole());
        form.setActive(user.isActive());
        return form;
    }

    @Transactional
    public UserAccount save(UserForm form) {
        UserAccount user;
        boolean creating = form.getId() == null;

        if (creating) {
            if (repository.existsByUsernameIgnoreCase(form.getUsername().trim())) {
                throw new BusinessException("That username is already in use.");
            }
            if (form.getPassword() == null || form.getPassword().length() < 8) {
                throw new BusinessException("A new user password must contain at least 8 characters.");
            }
            user = new UserAccount();
        } else {
            user = findById(form.getId());
            if (!user.getUsername().equalsIgnoreCase(form.getUsername().trim())
                    && repository.existsByUsernameIgnoreCase(form.getUsername().trim())) {
                throw new BusinessException("That username is already in use.");
            }
            protectLastAdministrator(user, form);
        }

        user.setFullName(form.getFullName().trim());
        user.setUsername(form.getUsername().trim());
        user.setRole(form.getRole());
        user.setActive(form.isActive());

        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            if (form.getPassword().length() < 8) {
                throw new BusinessException("Password must contain at least 8 characters.");
            }
            user.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        UserAccount saved = repository.save(user);
        auditService.record(creating ? "USER_CREATE" : "USER_UPDATE",
                saved.getUsername() + " (" + saved.getRole() + ")");
        return saved;
    }

    private void protectLastAdministrator(UserAccount existing, UserForm requested) {
        boolean removingActiveAdmin = existing.getRole() == Role.ADMIN
                && existing.isActive()
                && (requested.getRole() != Role.ADMIN || !requested.isActive());
        if (removingActiveAdmin && repository.countByRoleAndActiveTrue(Role.ADMIN) <= 1) {
            throw new BusinessException("At least one active administrator account must remain in the system.");
        }
    }
}
