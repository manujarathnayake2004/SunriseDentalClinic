package lk.sunrise.clinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class InitialAdminForm {
    @NotBlank(message = "Full name is required.")
    @Size(max = 60, message = "Full name must be 60 characters or fewer.")
    private String fullName;

    @NotBlank(message = "Username is required.")
    @Size(min = 4, max = 50, message = "Username must contain 4 to 50 characters.")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Username may contain letters, numbers, dot, underscore and hyphen only.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 100, message = "Password must contain at least 8 characters.")
    private String password;

    @NotBlank(message = "Confirm the password.")
    private String confirmPassword;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
