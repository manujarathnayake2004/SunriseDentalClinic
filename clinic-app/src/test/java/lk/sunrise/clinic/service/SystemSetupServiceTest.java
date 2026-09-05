package lk.sunrise.clinic.service;

import lk.sunrise.clinic.dto.InitialAdminForm;
import lk.sunrise.clinic.exception.BusinessException;
import lk.sunrise.clinic.model.Role;
import lk.sunrise.clinic.model.UserAccount;
import lk.sunrise.clinic.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SystemSetupServiceTest {
    private UserAccountRepository repository;
    private PasswordEncoder passwordEncoder;
    private SystemSetupService service;

    @BeforeEach
    void setUp() {
        repository = mock(UserAccountRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        service = new SystemSetupService(repository, passwordEncoder);
    }

    @Test
    void shouldCreateInitialAdministratorWithoutDefaultCredentials() {
        when(repository.count()).thenReturn(0L);
        when(passwordEncoder.encode("SecurePass1")).thenReturn("encoded-password");
        when(repository.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAccount saved = service.createInitialAdministrator(validForm());

        assertEquals(Role.ADMIN, saved.getRole());
        assertTrue(saved.isActive());
        assertEquals("encoded-password", saved.getPassword());
        assertEquals("clinic.admin", saved.getUsername());
    }

    @Test
    void shouldRejectSecondInitialSetup() {
        when(repository.count()).thenReturn(1L);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.createInitialAdministrator(validForm()));

        assertTrue(ex.getMessage().contains("already been completed"));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldRejectMismatchedPasswordConfirmation() {
        when(repository.count()).thenReturn(0L);
        InitialAdminForm form = validForm();
        form.setConfirmPassword("DifferentPass1");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.createInitialAdministrator(form));

        assertTrue(ex.getMessage().contains("do not match"));
        verify(repository, never()).save(any());
    }

    private InitialAdminForm validForm() {
        InitialAdminForm form = new InitialAdminForm();
        form.setFullName("Clinic Administrator");
        form.setUsername("clinic.admin");
        form.setPassword("SecurePass1");
        form.setConfirmPassword("SecurePass1");
        return form;
    }
}
