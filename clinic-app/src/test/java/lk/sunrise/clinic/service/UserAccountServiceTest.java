package lk.sunrise.clinic.service;

import lk.sunrise.clinic.dto.UserForm;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.Role;
import lk.sunrise.clinic.model.UserAccount;
import lk.sunrise.clinic.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAccountServiceTest {
    private UserAccountRepository repository;
    private PasswordEncoder passwordEncoder;
    private AuditService auditService;
    private UserAccountService service;

    @BeforeEach
    void setUp() {
        repository = mock(UserAccountRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        auditService = mock(AuditService.class);
        service = new UserAccountService(repository, passwordEncoder, auditService);
    }

    @Test
    void shouldProtectLastActiveAdministrator() {
        UserAccount administrator = new UserAccount();
        administrator.setFullName("Clinic Administrator");
        administrator.setUsername("clinic.admin");
        administrator.setPassword("encoded");
        administrator.setRole(Role.ADMIN);
        administrator.setActive(true);

        when(repository.findById(1L)).thenReturn(Optional.of(administrator));
        when(repository.countByRoleAndActiveTrue(Role.ADMIN)).thenReturn(1L);

        UserForm form = new UserForm();
        form.setId(1L);
        form.setFullName("Clinic Administrator");
        form.setUsername("clinic.admin");
        form.setRole(Role.RECEPTIONIST);
        form.setActive(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.save(form));

        assertTrue(ex.getMessage().contains("At least one active administrator"));
        verify(repository, never()).save(any());
    }
}
