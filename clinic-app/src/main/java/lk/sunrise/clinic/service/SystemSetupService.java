package lk.sunrise.clinic.service;

import lk.sunrise.clinic.dto.InitialAdminForm;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.Role;
import lk.sunrise.clinic.model.UserAccount;
import lk.sunrise.clinic.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemSetupService {
    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public SystemSetupService(UserAccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean setupRequired() {
        return repository.count() == 0;
    }

    @Transactional
    public UserAccount createInitialAdministrator(InitialAdminForm form) {
        if (!setupRequired()) {
            throw new BusinessException("Initial system setup has already been completed.");
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new BusinessException("Password and confirmation do not match.");
        }

        UserAccount administrator = new UserAccount();
        administrator.setFullName(form.getFullName().trim());
        administrator.setUsername(form.getUsername().trim());
        administrator.setPassword(passwordEncoder.encode(form.getPassword()));
        administrator.setRole(Role.ADMIN);
        administrator.setActive(true);
        return repository.save(administrator);
    }
}
