package lk.sunrise.clinic.repository;

import lk.sunrise.clinic.model.Role;
import lk.sunrise.clinic.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);
    long countByRoleAndActiveTrue(Role role);
}
