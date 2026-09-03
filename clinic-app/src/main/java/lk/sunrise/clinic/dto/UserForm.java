package lk.sunrise.clinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lk.sunrise.clinic.model.Role;

public class UserForm {
    private Long id;

    @NotBlank(message = "Full name is required.")
    @Size(max = 60, message = "Full name must be 60 characters or fewer.")
    private String fullName;

    @NotBlank(message = "Username is required.")
    @Size(min = 4, max = 50, message = "Username must contain 4 to 50 characters.")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Username may contain letters, numbers, dot, underscore and hyphen only.")
    private String username;

    @Size(max = 100)
    private String password;

    @NotNull(message = "Role is required.")
    private Role role;

    private boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
