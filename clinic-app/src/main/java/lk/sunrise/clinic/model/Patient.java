package lk.sunrise.clinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "patients", uniqueConstraints = @UniqueConstraint(columnNames = "patient_number"))
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_number", nullable = false, unique = true, length = 20)
    private String patientNumber;

    @NotBlank(message = "Patient name is required.")
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String patientName;

    @NotBlank(message = "Address is required.")
    @Size(max = 250)
    @Column(nullable = false, length = 250)
    private String address;

    @NotBlank(message = "Contact number is required.")
    @Pattern(regexp = "^[0-9+()\\-\\s]{7,20}$", message = "Enter a valid contact number.")
    @Column(nullable = false, length = 20)
    private String contactNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPatientNumber() { return patientNumber; }
    public void setPatientNumber(String patientNumber) { this.patientNumber = patientNumber; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
